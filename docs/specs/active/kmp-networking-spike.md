# Spec: kmp-networking-spike

Status: in-progress
Created: 2026-07-09

## Strategic context

The `greeting` module and mobile demo UI are **disposable test harnesses**, not a product.
This spike's real deliverable is verified, repeatable SDD infrastructure: spec → OpenAPI
contract → Modulith module endpoint → KMP shared client → native UI. Future apps and
modules should follow the same pattern; greeting exists only to prove the toolchain.

## Problem

Checkpoints 1 and 2 proved the backend (Spring Modulith + `greeting` module) and mobile
(KMP `sharedLogic` callable from native Android and iOS) work in isolation. There is no
HTTP surface on the backend yet (`GreetingApi` is an in-process interface, not a REST
controller), no `contracts/openapi.yaml`, and no networking code in `mobile/sharedLogic`.
We cannot verify the full cross-stack toolchain — contract → backend endpoint → KMP HTTP
client → native UI — until this plumbing exists.

## Non-goals

- **Web client.** No `web/` directory exists yet. Creating `contracts/openapi.yaml` is
  in scope as the API source of truth, but wiring a web client is explicitly deferred.
  (AGENTS.md normally requires web + mobile client updates with contract changes; this
  spike is an accepted exception because web does not exist.)
- **Production error handling** — no retry logic, offline mode, structured error types,
  or user-facing error copy beyond a minimal failure string if the call fails.
- **UI polish** — keep existing checkpoint-2 UI patterns (button reveals content); no new
  screens, navigation, or design work.
- **OpenAPI code generation** — document the endpoint in OpenAPI; implement the KMP
  client by hand with Ktor for this spike. Codegen tooling is a follow-up once the
  pattern is proven.
- **Persistence, auth, CORS, HTTPS/TLS** — plain HTTP to a local dev server is fine.
- **CI workflow changes** — no new GitHub Actions in this spike unless required to keep
  existing checks green.
- **Replacing or removing `sharedUI`** — Android continues to use the existing
  `sharedUI` `App()` composable; only the data source changes from local to remote.

## Approach

**Backend:** Add a REST controller in the `greeting` module's public package (not
`internal`) that exposes the existing `GreetingService` over HTTP. The greeting module
will need `spring-boot-starter-web` (or equivalent) added to its own `build.gradle.kts`
since the convention plugin does not include web support. Proposed shape:

```
GET /api/greeting?name={name}
→ 200 { "message": "Hello, {name}, from a Spring Modulith module." }
```

**Contract:** Create `contracts/openapi.yaml` (file does not exist yet) documenting this
single endpoint and response schema. This establishes the contract-first pattern even
though only mobile consumes it for now.

**Mobile (`sharedLogic`):** Add Ktor Client (`ktor-client-core` + platform engines:
`ktor-client-okhttp` for Android, `ktor-client-darwin` for iOS) as the first networking
dependencies in the mobile version catalog. Introduce a small shared API client (e.g.
`GreetingClient` or `ApiClient`) with a `suspend fun fetchGreeting(name: String): String`
that performs the GET, parses JSON, and returns the message. Base URL is supplied via
`expect`/`actual` or a simple config object so Android emulator can use `10.0.2.2:8080`
while iOS simulator uses `localhost:8080`. Extend or replace the existing `Greeting`
class so platform UIs call the remote fetch instead of the local `sayHello()` helper.

**Android:** Add `INTERNET` permission and allow cleartext HTTP to localhost in
`AndroidManifest.xml` (dev spike only). Update `sharedUI` `App()` to trigger the suspend
fetch (e.g. `LaunchedEffect` + coroutines) and display the backend response string.

**iOS:** Add App Transport Security exception for localhost HTTP in `Info.plist`. Update
`ContentView.swift` to call the shared Kotlin suspend function (via coroutines bridge or
a simple callback wrapper exported from `sharedLogic`) and display the result.

**Verification:** Backend gets a `@SpringBootTest` + `MockMvc` integration test (no
Postgres/Testcontainers — this endpoint is stateless). `sharedLogic` gets a `commonTest`
using Ktor `MockEngine` to verify request shape and response parsing without a running
server. Manual smoke test: run backend locally, launch Android emulator and iOS simulator,
confirm both UIs show the backend greeting string.

## Acceptance criteria

- [ ] `GET /api/greeting?name=Android` returns HTTP 200 with JSON body
      `{ "message": "Hello, Android, from a Spring Modulith module." }` when the backend
      is running locally.
- [ ] `contracts/openapi.yaml` exists and describes the greeting endpoint and response
      schema; it matches the implemented backend behavior.
- [ ] `mobile/sharedLogic` contains a shared HTTP client that calls the greeting endpoint
      and parses the response — not a platform-specific fetch in `androidApp` or `iosApp`.
- [ ] Android app (emulator, backend running on host) displays the backend greeting
      message (not the local `sayHello()` string) after user interaction triggers the fetch.
- [ ] iOS app (simulator, backend running on host) displays the same backend greeting
      message via the same shared client code path.
- [ ] `ModularityTests` still passes — greeting controller and service remain within
      module boundaries.
- [ ] Backend integration test (MockMvc) passes for the new endpoint.
- [ ] `sharedLogic` commonTest with Ktor MockEngine passes for the client.
- [ ] `./gradlew :backend:test` and `./gradlew :sharedLogic:allTests` (or equivalent
      mobile test tasks) pass.

## Tasks

- [ ] **Backend:** Add `spring-boot-starter-web` to `backend/modules/greeting/build.gradle.kts`.
- [ ] **Backend:** Create `GreetingController` in `com.yourorg.stackspike.greeting` (public
      package) with `GET /api/greeting`, delegating to `GreetingApi`.
- [ ] **Backend:** Add response DTO record/class in the public package.
- [ ] **Backend:** Add `@SpringBootTest` + MockMvc integration test for the endpoint.
- [ ] **Backend:** Run `./gradlew :backend:test --tests ModularityTests` and full backend
      test suite.
- [ ] **Contract:** Create `contracts/openapi.yaml` with the greeting endpoint spec.
- [ ] **Mobile/sharedLogic:** Add Ktor client dependencies to `mobile/gradle/libs.versions.toml`
      and `sharedLogic/build.gradle.kts` (ask before adding if versions are not already present).
- [ ] **Mobile/sharedLogic:** Implement base-URL `expect`/`actual` for Android vs iOS localhost.
- [ ] **Mobile/sharedLogic:** Implement `GreetingClient` (or equivalent) with suspend fetch + JSON parse.
- [ ] **Mobile/sharedLogic:** Wire `Greeting` (or new facade) to call remote API; keep platform
      name as the `name` query parameter.
- [ ] **Mobile/sharedLogic:** Add `commonTest` with Ktor `MockEngine` covering happy path.
- [ ] **Android:** Add `INTERNET` permission and cleartext-traffic allowance for localhost.
- [ ] **Android:** Update `sharedUI` `App()` to fetch and display remote greeting asynchronously.
- [ ] **iOS:** Add ATS localhost exception to `Info.plist`.
- [ ] **iOS:** Update `ContentView.swift` to fetch and display remote greeting from sharedLogic.
- [ ] **Tests:** Manual smoke checklist documented in PR or spec notes (backend up → Android
      emulator → iOS simulator).

## Open questions

1. **Ktor dependency approval.** Ktor is the de facto KMP HTTP client but is not yet in
   `mobile/gradle/libs.versions.toml`. Confirm version choice before implementation.
2. **iOS coroutine bridge.** Calling `suspend` Kotlin from SwiftUI requires a small
   wrapper (e.g. `suspendWrapper` with completion handler, or SKIE/KMP-NativeCoroutines).
   Pick the lightest option that works for the spike — avoid introducing a heavy bridge
   library unless necessary.
3. **Endpoint path prefix.** `/api/greeting` is proposed; confirm or adjust before coding
   so OpenAPI and controller stay aligned.

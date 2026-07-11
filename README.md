# quickapp

**Spec-driven starter** for full-stack products. Clone this repo to begin a new app
with a proven toolchain — not a one-off spike.

| Layer | Stack |
|-------|--------|
| Backend | Java 25, Spring Boot 4.1, Spring Modulith (vertical modules) |
| Mobile | Kotlin Multiplatform — shared logic, Android (Compose) + iOS (SwiftUI) |
| Contract | OpenAPI (`contracts/openapi.yaml`) |
| Web | Not scaffolded yet |
| Workflow | `/spec` → `/implement` → archive |

The `greeting` module and demo UI are **disposable harnesses** that prove the
cross-stack path works. Real features get their own Modulith modules and specs.

## Quick start

```bash
# Backend (repo root) — leave running
./gradlew :backend:bootRun

# Verify API
curl "http://localhost:8080/api/greeting?name=Android"
```

**Android:** open `mobile/` in Android Studio → run `androidApp` on an emulator.

**iOS:** open `mobile/iosApp/iosApp.xcodeproj` in Xcode → run on a simulator.

Success looks like a greeting ending in `from a Spring Modulith module.` (proves a
real HTTP round-trip).

## Docs

| Doc | Purpose |
|-----|---------|
| [AGENTS.md](AGENTS.md) | Constitution for humans and coding agents |
| [docs/architecture.md](docs/architecture.md) | SDD workflow, module patterns, how to add features |
| [docs/specs/](docs/specs/) | Active and archived feature specs |
| [contracts/openapi.yaml](contracts/openapi.yaml) | API source of truth |

## Tests

```bash
# Backend
./gradlew :backend:test

# Mobile (from mobile/)
cd mobile
./gradlew :sharedLogic:testAndroidHostTest :androidApp:assembleDebug
```

## Starting a new product from this repo

1. Clone / copy **quickapp** as your new app repo.
2. Read `docs/architecture.md` (“Adding a real feature”).
3. `/spec <feature-name>` for the first real capability (or CI / web scaffold).
4. Keep `greeting` until you no longer need the harness, then delete it.

## Status

Verified checkpoints: Modulith backend, KMP sharedLogic on both platforms,
end-to-end networking (OpenAPI → REST → Ktor → native UI), and path-filtered CI.
`main` is PR-protected (one feature branch per active spec). Next infrastructure:
web client.

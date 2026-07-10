# AGENTS.md — [PROJECT NAME]

This file is the constitution. It changes rarely — only when a real architectural
decision changes, not per-feature.

## What this is

[ONE PARAGRAPH: what the app does, who it's for.]

## Stack

- Backend: Java 25, Spring Boot 4.1, Gradle (Kotlin DSL) with a `build-logic`
  included build providing convention plugins — not Maven.
- Backend architecture: Spring Modulith — a modular monolith, not a layered
  api/domain/infra split. Modules are vertical slices (one per business capability),
  auto-discovered from `backend/modules/*` by `settings.gradle.kts`.
- Mobile: Kotlin Multiplatform. `mobile/sharedLogic` holds shared business logic
  and networking, consumed by both `mobile/androidApp` (Jetpack Compose) and
  `mobile/iosApp` (native SwiftUI). `mobile/sharedUI` exists from the project
  wizard's default scaffolding but is not required — evaluate whether to keep
  using it or go fully native-UI per platform as real screens get built.
- Mobile and backend are two independent Gradle builds in one git repo, not one
  unified build. They share no Gradle code — only the OpenAPI contract connects them.
- Web: [React/Vue] + reactive state, AI-driven, lower rigor than backend/mobile —
  entirely separate toolchain, not part of either Gradle build.
- Contract: OpenAPI spec in `contracts/openapi.yaml` — source of truth for the API.
- CI/CD: GitHub Actions, path-filtered per module (see `.github/workflows/`).
- Hosting: Render (backend), Neon (Postgres), UptimeRobot (keep-alive).

## Module boundaries (backend)

- Each folder under `backend/modules/` is one Spring Modulith module — a vertical
  slice (e.g. `polls`, `voting`), not a horizontal layer.
- Within a module: an `internal` sub-package is invisible to every other module.
  Anything meant to be called from outside the module (an interface, a DTO) lives
  directly in the module's top-level package, not in `internal`.
- Cross-module communication happens via Spring application events, not direct
  method calls into another module's internals.
- Enforced automatically by `ModularityTests` (`backend/src/test/.../ModularityTests.java`),
  which calls `ApplicationModules.verify()`. This must pass before any backend PR merges.
- New module checklist: create `backend/modules/<name>/`, add a
  `build.gradle.kts` containing only `plugins { id("quickapp.module-conventions") }`,
  write code with an `internal` sub-package for anything not meant to be public.
  No edits to `settings.gradle.kts` — module discovery is automatic.

## The build-logic convention plugin

- `build-logic/src/main/kotlin/quickapp.module-conventions.gradle.kts` is what
  every backend module applies. It bundles: Java 25 toolchain, the Spring Boot
  BOM (via `dependencyManagement`), the `spring-context` dependency, JUnit 5
  test execution, and `spring-boot-starter-test`.
- If a module needs something the convention plugin doesn't provide, add the
  dependency directly in that module's own `build.gradle.kts` — don't fork the
  convention plugin per-module. Only promote something into the shared convention
  once at least two modules need it.
- Convention plugins cannot use the generated `libs.x.y` version-catalog shortcut
  — this is a genuine Gradle limitation, not a mistake. Use
  `extensions.getByType<VersionCatalogsExtension>().named("libs")` instead, and
  look up entries with `.findLibrary(...)` / `.findVersion(...)`.

## Non-negotiables

- `ModularityTests` must pass before any backend PR merges — module boundaries
  are enforced by a test, not convention.
- Every new endpoint or service method ships with a test in the same PR.
- Never modify `contracts/openapi.yaml` without updating both `web` and `mobile`
  client code in the same change.
- No placeholder implementations. If you can't finish something, stop and say so.
- Before any multi-file change, create a git checkpoint commit first.
- Don't invent a dependency. Check the relevant `build.gradle.kts` / `libs.versions.toml`
  / `package.json` first. If it's new, ask before adding it.

## How specs work here

- `docs/specs/active/<feature>.md` via `/spec`, followed via `/implement`.
- `docs/specs/archive/` — completed specs, kept as history.
- `docs/architecture.md` — longer-lived design decisions.

## Conventions

[FILL IN AS YOU GO — add an entry only after the same mistake happens twice.]

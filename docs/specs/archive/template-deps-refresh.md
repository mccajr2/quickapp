# Spec: template-deps-refresh

Status: complete  
Created: 2026-08-07  
Added: 2026-08-07 · enhancement  
Parent: omit (infra one-off for the starter template; keep product backlog empty)

## Problem

Dependabot opened eight first-wave PRs after `template-ci-hygiene` landed.
Four web lockfile PRs will thrash if merged separately; TypeScript 7 fails CI
because `baseUrl` was removed; and the two mobile PRs cannot go green without
raising AGP and `compileSdk` (new lifecycle/Compose artifacts require AGP ≥ 9.1
and compileSdk ≥ 37 while the template is still on AGP 9.0.1 / SDK 36). Leaving
them open leaves the template behind and trains every new app on stale pins.

## Non-goals

- Product features, greeting harness removal, or a filled roadmap backlog
- Changing Dependabot group rules / ignore lists in `.github/dependabot.yml`
- iOS Xcode project or CocoaPods/SPM bumps beyond what the mobile Gradle catalog
  already drives
- Migrating Compose Multiplatform off prerelease Material3 alphas beyond what
  Dependabot already proposed
- Root Spring Boot major bumps (none open)

## Approach

Land everything in **one** feature branch / PR so overlapping lockfiles and the
mobile toolchain bump stay coherent, then close the eight Dependabot PRs as
superseded:

1. **Backend (PR #11):** Spring Modulith `2.0.2` → `2.1.0` in
   `gradle/libs.versions.toml`.
2. **Actions (PR #12):** `actions/checkout` and `actions/setup-node` → v7 in
   backend/mobile/web workflows (`secrets.yml` already on checkout@v7).
3. **Web:** apply production + development minor/patch bumps, `@types/node` 26,
   and TypeScript 7 in one `npm install` under Corepack / `web/.nvmrc`. Fix TS 7
   by removing `baseUrl` and `ignoreDeprecations: "6.0"` from
   `web/tsconfig.app.json` (keep existing `@/*` → `./src/*` paths; Vite alias
   unchanged).
4. **Mobile (PRs #17 + #18):** apply the Dependabot catalog bumps **and** raise
   `agp` to ≥ `9.1.0` and `android-compileSdk` (and `android-targetSdk` if kept
   in lockstep) to `37` in `mobile/gradle/libs.versions.toml` so AAR metadata
   checks pass. Refresh the Gradle wrapper if Dependabot’s wrapper bump is
   included.
5. Close Dependabot PRs #11–#18 with a pointer to this PR after merge (or once
   this PR is open and green).

No OpenAPI / contract changes.

## Acceptance criteria

- [x] Spring Modulith is `2.1.0` (core + test starters via shared version ref)
- [x] Backend, mobile, and web workflows use `actions/checkout@v7`; web uses
      `actions/setup-node@v7`
- [x] Web production and development Dependabot bumps from #13/#14 are applied
- [x] `@types/node` is on the 26.x line; `typescript` is on 7.x
- [x] `web/tsconfig.app.json` has no `baseUrl`; `@/*` path mapping still works
- [x] `cd web && npm ci && npm test && npm run lint && npm run build` pass
- [x] Mobile catalog includes lifecycle `2.11.0`, the #17 group bumps, AGP ≥ 9.1.0,
      and compileSdk 37
- [x] `cd mobile && ./gradlew :sharedLogic:testAndroidHostTest :androidApp:assembleDebug`
      passes (same command as CI)
- [x] `./gradlew :backend:test` passes (at least Modulith + existing suite)
- [x] Dependabot PRs #11–#18 are closed as superseded by this change (after this
      PR is open/merged)

## Tasks

- [x] Backend: bump Spring Modulith in `gradle/libs.versions.toml`
- [x] CI: bump checkout/setup-node in `.github/workflows/{backend,mobile,web}.yml`
- [x] Web: apply #13/#14/#15/#16 version pins in `web/package.json` + lockfile
- [x] Web: remove `baseUrl` + `ignoreDeprecations` for TypeScript 7
- [x] Mobile: apply #17/#18 catalog bumps; set AGP ≥ 9.1.0 and compileSdk/targetSdk 37
- [x] Mobile: apply Gradle wrapper bump if present in #17
- [x] Tests: backend `:backend:test`; web test/lint/build; mobile host tests + assembleDebug
- [x] Housekeeping: open this PR; close Dependabot #11–#18 as superseded

## Open questions

- None blocking — accept AGP 9.1+ / SDK 37 as required for the mobile library
  floor Dependabot selected. If AGP 9.1.x is not yet published under the pin we
  try first, take the newest 9.1+ that resolves and note it in the PR.

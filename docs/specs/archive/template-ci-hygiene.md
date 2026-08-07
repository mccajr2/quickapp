# Spec: template-ci-hygiene

Status: complete  
Created: 2026-08-07  
Added: 2026-08-07 · enhancement  
Parent: omit (infra one-off for the starter template; keep product backlog empty)

## Problem

App repos created from quickapp inherit path-filtered CI that can miss
`contracts/**`-only PRs, have no secret scanning, no Dependabot version
updates, and a jsdom setup that lacks `matchMedia`. Food Investigators already
proved these fixes; the template should ship them so every new app starts clean.

## Non-goals

- Product features, greeting harness removal, or a filled roadmap backlog
- Dockerfile / Render hosting skeleton (optional follow-up)
- Full OpenAPI contract suite (only structural CI workflow assertions)
- Dependency vuln/license scanners, Spectral, or `ci-cd-production`

## Approach

Port proven FI hygiene into the template:

1. `.github/workflows/secrets.yml` (gitleaks, no path filters, pinned action)
2. `contracts/**` on backend workflow path filters
3. `.github/dependabot.yml` (weekly grouped npm/Gradle/Actions)
4. `matchMedia` stub in `web/src/test/setup.ts`
5. Architecture CI table updates + `CiWorkflowContractTest` structural tests

## Acceptance criteria

- [x] Secrets workflow exists, pinned gitleaks Action, no `paths:` under `on:`
- [x] Backend workflow path filters include `contracts/**` (push + PR)
- [x] Dependabot config present for web npm, root Gradle, mobile Gradle, Actions
- [x] Web Vitest setup stubs `matchMedia` when missing
- [x] Architecture CI docs mention secrets + contracts path filter
- [x] Structural JUnit tests cover secrets + contracts filters

## Tasks

- [x] CI: secrets.yml + architecture note
- [x] CI: contracts/** on backend.yml + architecture note
- [x] CI: dependabot.yml
- [x] Web: matchMedia stub in test setup
- [x] Tests: CiWorkflowContractTest (secrets + contracts filters)

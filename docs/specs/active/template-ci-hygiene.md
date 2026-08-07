# Spec: template-ci-hygiene

Status: in-progress  
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

- [ ] Secrets workflow exists, pinned gitleaks Action, no `paths:` under `on:`
- [ ] Backend workflow path filters include `contracts/**` (push + PR)
- [ ] Dependabot config present for web npm, root Gradle, mobile Gradle, Actions
- [ ] Web Vitest setup stubs `matchMedia` when missing
- [ ] Architecture CI docs mention secrets + contracts path filter
- [ ] Structural JUnit tests cover secrets + contracts filters

## Tasks

- [ ] CI: secrets.yml + architecture note
- [ ] CI: contracts/** on backend.yml + architecture note
- [ ] CI: dependabot.yml
- [ ] Web: matchMedia stub in test setup
- [ ] Tests: CiWorkflowContractTest (secrets + contracts filters)

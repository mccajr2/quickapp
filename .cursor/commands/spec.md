Create a new feature spec.

Argument: feature name (kebab-case), e.g. `add-password-reset`

Steps:
1. Copy `docs/specs/_template.md` to `docs/specs/active/<feature-name>.md`.
2. Ask me clarifying questions ONE AT A TIME if the feature request is ambiguous —
   specifically about: what's explicitly out of scope, whether this touches the
   OpenAPI contract, and what "done" looks like. Do not proceed to writing the
   spec until scope is clear enough to write real (not vague) acceptance criteria.
3. Fill in Problem, Non-goals, Approach, Acceptance Criteria, and a Tasks checklist
   broken down by layer (backend/contract/web/iOS/tests) — omit layers this feature
   doesn't touch.
4. Do NOT start implementing. Stop after the spec is written and show it to me for
   approval before any code changes.

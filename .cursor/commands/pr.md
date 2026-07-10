Open a pull request for the current changes.

Steps:
1. Run `git diff` and `git diff --staged` to see everything changed.
2. Confirm all relevant tests pass (per the layer(s) touched — see
   `.cursor/rules/backend.mdc` / `web.mdc` / `ios.mdc` for the right command).
   Do not proceed if something fails; report it instead.
3. Write a clear commit message: what changed and why, referencing the spec file
   if one exists (`docs/specs/active/<feature>.md`).
4. Commit and push to the current branch.
5. If the spec for this feature is fully checked off, move it from
   `docs/specs/active/` to `docs/specs/archive/` in the same commit.
6. Open the PR with `gh pr create`, using the spec's Problem/Acceptance Criteria
   sections as the PR description if a spec exists.
7. Return the PR URL.

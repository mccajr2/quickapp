# web

Vite + React + TypeScript client for quickapp.

## Toolchain

Pinned like a wrapper — do not improvise versions:

| Pin | Where |
|-----|--------|
| Node `24.4.1` | `web/.nvmrc`, `package.json` → `engines` |
| npm `11.4.2` | `package.json` → `packageManager` + `engines` |

```bash
# Use the Node in .nvmrc (nvm/fnm/asdf), then:
corepack enable
cd web
npm ci          # preferred when lockfile exists
npm run dev
```

`engine-strict=true` in `.npmrc` fails installs on the wrong Node/npm.  
Only commit `package-lock.json` updates produced with this toolchain. CI runs
`corepack enable` + `npm ci` only (never `npm install`).

## Quick start

```bash
cp .env.example .env   # optional
corepack enable
npm ci                 # or npm install on first bootstrap of a new lockfile
npm run dev
```

Scripts: `dev`, `build`, `lint`, `test`.

In `npm run dev`, `/api` is proxied to `http://localhost:8080` (avoids CORS).
Override with `VITE_API_BASE_URL` when you need a different origin.

With the backend running (`./gradlew :backend:bootRun` from the repo root), open
the app and fetch a greeting — success text includes
`from a Spring Modulith module.`

# web

Vite + React + TypeScript client for quickapp.

```bash
cp .env.example .env   # optional; defaults to http://localhost:8080
npm install
npm run dev
```

Scripts: `dev`, `build`, `lint`, `test`.

API calls go through `src/api/` only. Base URL: `VITE_API_BASE_URL` (see
`.env.example`; default `http://localhost:8080`).

With the backend running (`./gradlew :backend:bootRun` from the repo root), open
the app and fetch a greeting — success text includes
`from a Spring Modulith module.`

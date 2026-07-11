# web

Vite + React + TypeScript client for quickapp.

```bash
cp .env.example .env   # optional; defaults to http://localhost:8080
npm install
npm run dev
```

Scripts: `dev`, `build`, `lint`, `test`.

In `npm run dev`, `/api` is proxied to `http://localhost:8080` (avoids CORS).
Override with `VITE_API_BASE_URL` when you need a different origin.

With the backend running (`./gradlew :backend:bootRun` from the repo root), open
the app and fetch a greeting — success text includes
`from a Spring Modulith module.`

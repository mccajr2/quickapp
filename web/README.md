# web

Vite + React + TypeScript client for quickapp.

```bash
cp .env.example .env   # optional; defaults to http://localhost:8080
npm install
npm run dev
```

Scripts: `dev`, `build`, `lint`, `test`.

API calls go through `src/api/` (added with the greeting harness). Base URL:
`VITE_API_BASE_URL` (see `.env.example`).

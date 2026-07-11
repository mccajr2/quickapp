/** Backend origin for API calls. Override with VITE_API_BASE_URL. */
export const apiBaseUrl =
  import.meta.env.VITE_API_BASE_URL?.replace(/\/$/, "") ??
  "http://localhost:8080"

import { apiBaseUrl } from "@/config"
import type { GreetingResponse } from "@/api/types"

export class GreetingClient {
  private readonly baseUrl: string
  private readonly fetchFn: typeof fetch

  constructor(
    baseUrl: string = apiBaseUrl,
    fetchFn: typeof fetch = globalThis.fetch.bind(globalThis),
  ) {
    this.baseUrl = baseUrl
    this.fetchFn = fetchFn
  }

  async fetchGreeting(name: string): Promise<string> {
    const url = new URL("/api/greeting", ensureTrailingSlash(this.baseUrl))
    url.searchParams.set("name", name)

    const response = await this.fetchFn(url)
    if (!response.ok) {
      throw new Error(`Greeting request failed (${response.status})`)
    }

    const body = (await response.json()) as GreetingResponse
    if (typeof body.message !== "string") {
      throw new Error("Invalid greeting response")
    }
    return body.message
  }
}

function ensureTrailingSlash(baseUrl: string): string {
  return baseUrl.endsWith("/") ? baseUrl : `${baseUrl}/`
}

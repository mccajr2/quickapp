import { describe, expect, it } from "vitest"

import { apiBaseUrl } from "@/config"
import { cn } from "@/lib/utils"

describe("web scaffold", () => {
  it("merges tailwind classes via cn()", () => {
    expect(cn("px-2", "px-4")).toBe("px-4")
  })

  it("defaults apiBaseUrl to local backend", () => {
    expect(apiBaseUrl).toBe("http://localhost:8080")
  })
})

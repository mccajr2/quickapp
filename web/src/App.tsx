import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { apiBaseUrl } from "@/config"

function App() {
  return (
    <main className="mx-auto flex min-h-svh max-w-lg flex-col justify-center px-4 py-10">
      <Card>
        <CardHeader>
          <CardTitle>quickapp</CardTitle>
          <CardDescription>
            Web scaffold is ready. Greeting harness comes next.
          </CardDescription>
        </CardHeader>
        <CardContent className="text-sm text-muted-foreground">
          API base URL: <code className="text-foreground">{apiBaseUrl}</code>
        </CardContent>
      </Card>
    </main>
  )
}

export default App

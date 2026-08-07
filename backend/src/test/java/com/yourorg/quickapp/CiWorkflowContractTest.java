package com.yourorg.quickapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class CiWorkflowContractTest {

    @Test
    void backendWorkflowPathFiltersIncludeContracts() throws IOException {
        Path workflow = resolveBackendWorkflow();
        assertThat(workflow).exists();
        String yaml = Files.readString(workflow);

        // push + pull_request each list contracts/** so contracts-only PRs run OpenAPI checks.
        assertThat(yaml.split("contracts/\\*\\*", -1)).hasSize(3);
    }

    @Test
    void secretsWorkflowIsNotPathFilteredAndRunsGitleaks() throws IOException {
        Path workflow = resolveSecretsWorkflow();
        assertThat(workflow).exists();
        String yaml = Files.readString(workflow);

        assertThat(yaml).contains("gitleaks/gitleaks-action@v3.0.0");
        assertThat(yaml).contains("pull_request:");
        assertThat(yaml).contains("push:");
        // Secrets can land in any path — no path filters under on:.
        String onBlock = yaml.split("jobs:", 2)[0];
        assertThat(onBlock).doesNotContain("paths:");
        assertThat(yaml).doesNotContain("@main");
        assertThat(yaml).doesNotContain("@master");
    }

    @Test
    void pathFilteredWorkflowsUseCheckoutV7AndWebUsesSetupNodeV7() throws IOException {
        String backend = Files.readString(resolveBackendWorkflow());
        String mobile = Files.readString(resolveWorkflow("mobile.yml"));
        String web = Files.readString(resolveWorkflow("web.yml"));

        assertThat(backend).contains("actions/checkout@v7");
        assertThat(mobile).contains("actions/checkout@v7");
        assertThat(web).contains("actions/checkout@v7");
        assertThat(web).contains("actions/setup-node@v7");
        assertThat(backend).doesNotContain("actions/checkout@v4");
        assertThat(mobile).doesNotContain("actions/checkout@v4");
        assertThat(web).doesNotContain("actions/checkout@v4");
        assertThat(web).doesNotContain("actions/setup-node@v4");
    }

    private static Path resolveBackendWorkflow() {
        return resolveWorkflow("backend.yml");
    }

    private static Path resolveSecretsWorkflow() {
        return resolveWorkflow("secrets.yml");
    }

    private static Path resolveWorkflow(String filename) {
        Path fromBackend =
                Path.of("..", ".github", "workflows", filename).normalize().toAbsolutePath();
        if (Files.exists(fromBackend)) {
            return fromBackend;
        }
        return Path.of(".github", "workflows", filename).toAbsolutePath();
    }
}

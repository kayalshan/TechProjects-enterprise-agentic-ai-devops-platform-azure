package com.enterprise.agentic.agentorchestrator.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrchestratorDtoTest {

    @Test
    void shouldCreateOrchestratorRequest() {
        OrchestratorRequest request = new OrchestratorRequest("Error event", "auth-service");

        assertThat(request.logMessage()).isEqualTo("Error event");
        assertThat(request.serviceName()).isEqualTo("auth-service");
    }

    @Test
    void shouldCreateOrchestratorResponse() {
        OrchestratorResponse response = new OrchestratorResponse(
                "DB slow",
                "High latency",
                "MEDIUM",
                "Restart DB cluster",
                "db-service",
                "completed"
        );

        assertThat(response.rootCause()).isEqualTo("DB slow");
        assertThat(response.impact()).isEqualTo("High latency");
        assertThat(response.severity()).isEqualTo("MEDIUM");
        assertThat(response.suggestedAction()).isEqualTo("Restart DB cluster");
        assertThat(response.toolTriggered()).isEqualTo("db-service");
        assertThat(response.actionResult()).isEqualTo("completed");
    }
}

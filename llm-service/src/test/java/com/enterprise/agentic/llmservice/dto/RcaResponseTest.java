package com.enterprise.agentic.llmservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RcaResponseTest {

    @Test
    void shouldCreateRcaResponseWithAllFields() {
        String rootCause = "Database connection pool exhaustion";
        String impact = "High latency";
        String severity = "HIGH";
        String suggestedAction = "Restart DB";
        String toolName = "restart-service";

        RcaResponse response = new RcaResponse(rootCause, impact, severity, suggestedAction, toolName);

        assertEquals(rootCause, response.rootCause());
        assertEquals(impact, response.impact());
        assertEquals(severity, response.severity());
        assertEquals(suggestedAction, response.suggestedAction());
        assertEquals(toolName, response.toolName());
    }
}
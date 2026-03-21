package com.enterprise.agentic.llmservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RcaResponseTest {

    @Test
    void shouldCreateRcaResponseWithAllFields() {
        // Given
        String rootCause = "Database connection pool exhaustion";
        String solution = "Increase connection pool size";
        String confidence = "HIGH";

        // When
        RcaResponse response = new RcaResponse(rootCause, solution, confidence);

        // Then
        assertEquals(rootCause, response.rootCause());
        assertEquals(solution, response.solution());
        assertEquals(confidence, response.confidence());
    }

    @Test
    void shouldCreateEqualRcaResponses() {
        // Given
        RcaResponse response1 = new RcaResponse("DB issue", "Fix pool", "HIGH");
        RcaResponse response2 = new RcaResponse("DB issue", "Fix pool", "HIGH");

        // Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldCreateDifferentRcaResponses() {
        // Given
        RcaResponse response1 = new RcaResponse("DB issue", "Fix pool", "HIGH");
        RcaResponse response2 = new RcaResponse("Network issue", "Check firewall", "MEDIUM");

        // Then
        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldHandleNullValues() {
        // When
        RcaResponse response = new RcaResponse(null, null, null);

        // Then
        assertNull(response.rootCause());
        assertNull(response.solution());
        assertNull(response.confidence());
    }
}
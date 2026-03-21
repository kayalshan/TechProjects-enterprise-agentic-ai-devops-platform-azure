package com.enterprise.agentic.llmservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlmResponseTest {

    @Test
    void shouldCreateLlmResponseWithResponse() {
        // Given
        String response = "Root cause analysis: Database connection timeout";

        // When
        LlmResponse llmResponse = new LlmResponse(response);

        // Then
        assertEquals(response, llmResponse.response());
    }

    @Test
    void shouldHandleNullResponse() {
        // When
        LlmResponse response = new LlmResponse(null);

        // Then
        assertNull(response.response());
    }

    @Test
    void shouldCreateEqualResponses() {
        // Given
        LlmResponse response1 = new LlmResponse("Analysis result");
        LlmResponse response2 = new LlmResponse("Analysis result");

        // Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldCreateDifferentResponses() {
        // Given
        LlmResponse response1 = new LlmResponse("Result 1");
        LlmResponse response2 = new LlmResponse("Result 2");

        // Then
        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }
}
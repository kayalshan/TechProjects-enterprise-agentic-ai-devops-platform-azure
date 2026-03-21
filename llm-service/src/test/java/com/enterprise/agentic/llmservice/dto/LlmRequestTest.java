package com.enterprise.agentic.llmservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlmRequestTest {

    @Test
    void shouldCreateLlmRequestWithAllFields() {
        // Given
        String query = "Analyze this error log";
        String model = "gpt-4";
        double temperature = 0.7;

        // When
        LlmRequest request = new LlmRequest(query, model, temperature);

        // Then
        assertEquals(query, request.query());
        assertEquals(model, request.model());
        assertEquals(temperature, request.temperature());
    }

    @Test
    void shouldCreateEqualRequests() {
        // Given
        LlmRequest request1 = new LlmRequest("test query", "gpt-3.5-turbo", 0.5);
        LlmRequest request2 = new LlmRequest("test query", "gpt-3.5-turbo", 0.5);

        // Then
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void shouldCreateDifferentRequests() {
        // Given
        LlmRequest request1 = new LlmRequest("query1", "gpt-4", 0.7);
        LlmRequest request2 = new LlmRequest("query2", "gpt-3.5-turbo", 0.5);

        // Then
        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }
}
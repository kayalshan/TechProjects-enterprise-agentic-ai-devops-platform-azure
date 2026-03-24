package com.enterprise.agentic.ragservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RagResponseTest {

    @Test
    void shouldCreateRagResponseWithContext() {
        // Given
        String context = "Database overload during peak traffic hours";

        // When
        RagResponse response = new RagResponse(context);

        // Then
        assertEquals(context, response.context());
    }

    @Test
    void shouldHandleNullContext() {
        // When
        RagResponse response = new RagResponse(null);

        // Then
        assertNull(response.context());
    }

    @Test
    void shouldCreateEqualResponses() {
        // Given
        RagResponse response1 = new RagResponse("Database issue context");
        RagResponse response2 = new RagResponse("Database issue context");

        // Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldCreateDifferentResponses() {
        // Given
        RagResponse response1 = new RagResponse("DB context");
        RagResponse response2 = new RagResponse("Network context");

        // Then
        assertNotEquals(response1, response2);
        assertNotEquals(response1.hashCode(), response2.hashCode());
    }
}
package com.enterprise.agentic.ragservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RagRequestTest {

    @Test
    void shouldCreateRagRequestWithLogMessage() {
        // Given
        String logMessage = "ERROR: Database connection timeout";

        // When
        RagRequest request = new RagRequest(logMessage);

        // Then
        assertEquals(logMessage, request.logMessage());
    }

    @Test
    void shouldHandleNullLogMessage() {
        // When
        RagRequest request = new RagRequest(null);

        // Then
        assertNull(request.logMessage());
    }

    @Test
    void shouldCreateEqualRequests() {
        // Given
        RagRequest request1 = new RagRequest("ERROR: timeout");
        RagRequest request2 = new RagRequest("ERROR: timeout");

        // Then
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void shouldCreateDifferentRequests() {
        // Given
        RagRequest request1 = new RagRequest("ERROR: timeout");
        RagRequest request2 = new RagRequest("WARN: slow response");

        // Then
        assertNotEquals(request1, request2);
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }
}
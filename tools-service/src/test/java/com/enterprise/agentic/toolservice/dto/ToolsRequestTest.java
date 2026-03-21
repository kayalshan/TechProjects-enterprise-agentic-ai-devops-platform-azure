package com.enterprise.agentic.toolservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolsRequestTest {

    @Test
    void shouldCreateToolsRequestWithAllFields() {
        // Given
        String toolName = "restart-service";
        String target = "payment-service";
        String metadata = "immediate";

        // When
        ToolsRequest request = new ToolsRequest(toolName, target, metadata);

        // Then
        assertEquals(toolName, request.toolName());
        assertEquals(target, request.target());
        assertEquals(metadata, request.metadata());
    }

    @Test
    void shouldHandleNullMetadata() {
        // Given
        String toolName = "send-alert";
        String target = "monitoring-system";

        // When
        ToolsRequest request = new ToolsRequest(toolName, target, null);

        // Then
        assertEquals(toolName, request.toolName());
        assertEquals(target, request.target());
        assertNull(request.metadata());
    }

    @Test
    void shouldCreateEqualRequests() {
        // Given
        ToolsRequest request1 = new ToolsRequest("restart-service", "payment-service", "immediate");
        ToolsRequest request2 = new ToolsRequest("restart-service", "payment-service", "immediate");

        // Then
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}
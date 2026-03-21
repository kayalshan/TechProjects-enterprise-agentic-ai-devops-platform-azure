package com.enterprise.agentic.toolservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolsResponseTest {

    @Test
    void shouldCreateToolsResponseWithAllFields() {
        // Given
        String status = "SUCCESS";
        String message = "Service restarted successfully";

        // When
        ToolsResponse response = new ToolsResponse(status, message);

        // Then
        assertEquals(status, response.status());
        assertEquals(message, response.message());
    }

    @Test
    void shouldCreateEqualResponses() {
        // Given
        ToolsResponse response1 = new ToolsResponse("SUCCESS", "Operation completed");
        ToolsResponse response2 = new ToolsResponse("SUCCESS", "Operation completed");

        // Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldCreateDifferentResponses() {
        // Given
        ToolsResponse successResponse = new ToolsResponse("SUCCESS", "OK");
        ToolsResponse failedResponse = new ToolsResponse("FAILED", "Error occurred");

        // Then
        assertNotEquals(successResponse, failedResponse);
        assertNotEquals(successResponse.hashCode(), failedResponse.hashCode());
    }
}
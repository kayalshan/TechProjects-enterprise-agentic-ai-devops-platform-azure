package com.enterprise.agentic.llmservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlmRequestTest {

    @Test
    void shouldCreateLlmRequestWithLogAndContext() {
        String logMessage = "ERROR: Disk full";
        String context = "production";

        LlmRequest request = new LlmRequest(logMessage, context);

        assertEquals(logMessage, request.logMessage());
        assertEquals(context, request.context());
    }

    @Test
    void shouldSupportEquality() {
        LlmRequest request1 = new LlmRequest("a", "b");
        LlmRequest request2 = new LlmRequest("a", "b");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void shouldSupportInequality() {
        LlmRequest request1 = new LlmRequest("a", "b");
        LlmRequest request2 = new LlmRequest("x", "y");

        assertNotEquals(request1, request2);
    }
}
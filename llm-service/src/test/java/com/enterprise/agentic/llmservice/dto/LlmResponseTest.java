package com.enterprise.agentic.llmservice.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LlmResponseTest {

    @Test
    void shouldCreateLlmResponseWithAllFields() {
        String response = "OK";
        String provider = "openai";
        String model = "gpt-4";
        Long tokensUsed = 100L;
        Long latencyMs = 50L;
        LocalDateTime timestamp = LocalDateTime.now();

        LlmResponse llmResponse = new LlmResponse(response, provider, model, tokensUsed, latencyMs, timestamp);

        assertEquals(response, llmResponse.response());
        assertEquals(provider, llmResponse.provider());
        assertEquals(model, llmResponse.model());
        assertEquals(tokensUsed, llmResponse.tokensUsed());
        assertEquals(latencyMs, llmResponse.latencyMs());
        assertEquals(timestamp, llmResponse.timestamp());
    }
}
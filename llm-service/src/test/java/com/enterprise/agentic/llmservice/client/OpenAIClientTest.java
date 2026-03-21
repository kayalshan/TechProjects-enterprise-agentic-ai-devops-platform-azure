package com.enterprise.agentic.llmservice.client;

import com.enterprise.agentic.llmservice.dto.LlmRequest;
import com.enterprise.agentic.llmservice.dto.LlmResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class OpenAIClientTest {

    private final OpenAIClient client = new OpenAIClient();

    @Test
    void shouldGenerateOpenAIResponseSuccessfully() {
        // Given
        LlmRequest request = new LlmRequest("OpenAI analysis request", "gpt-3.5-turbo", 0.7);

        // When
        Mono<LlmResponse> result = client.generate(request);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response != null &&
                        response.response() != null)
                .verifyComplete();
    }

    @Test
    void shouldHandleHighTemperature() {
        // Given
        LlmRequest request = new LlmRequest("Creative analysis", "gpt-4", 0.9);

        // When
        Mono<LlmResponse> result = client.generate(request);

        // Then
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldHandleLowTemperature() {
        // Given
        LlmRequest request = new LlmRequest("Precise analysis", "gpt-3.5-turbo", 0.1);

        // When
        Mono<LlmResponse> result = client.generate(request);

        // Then
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }
}
package com.enterprise.agentic.llmservice.client;

import com.enterprise.agentic.llmservice.dto.LlmRequest;
import com.enterprise.agentic.llmservice.dto.LlmResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class LlmClientTest {

    private final LlmClient client = new LlmClient();

    @Test
    void shouldGenerateResponseSuccessfully() {
        // Given
        LlmRequest request = new LlmRequest("What is the root cause?", "gpt-3.5-turbo", 0.5);

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
    void shouldHandleDifferentModels() {
        // Given
        LlmRequest gpt4Request = new LlmRequest("Analyze error", "gpt-4", 0.8);
        LlmRequest gpt35Request = new LlmRequest("Analyze error", "gpt-3.5-turbo", 0.5);

        // When & Then
        StepVerifier.create(client.generate(gpt4Request))
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(client.generate(gpt35Request))
                .expectNextCount(1)
                .verifyComplete();
    }
}
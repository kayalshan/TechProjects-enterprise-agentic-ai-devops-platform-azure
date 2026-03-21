package com.enterprise.agentic.llmservice.client;

import com.enterprise.agentic.llmservice.dto.LlmRequest;
import com.enterprise.agentic.llmservice.dto.LlmResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class LocalLlmClientTest {

    private final LocalLlmClient client = new LocalLlmClient();

    @Test
    void shouldGenerateLocalResponseSuccessfully() {
        // Given
        LlmRequest request = new LlmRequest("Local analysis request", "local-model", 0.3);

        // When
        Mono<LlmResponse> result = client.generate(request);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response != null &&
                        response.response() != null &&
                        response.response().contains("local") ||
                        response.response().contains("Local"))
                .verifyComplete();
    }

    @Test
    void shouldHandleEmptyRequest() {
        // Given
        LlmRequest request = new LlmRequest("", "local-model", 0.5);

        // When
        Mono<LlmResponse> result = client.generate(request);

        // Then
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }
}
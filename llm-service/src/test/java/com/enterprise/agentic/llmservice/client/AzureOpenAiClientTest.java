package com.enterprise.agentic.llmservice.client;

import com.enterprise.agentic.llmservice.dto.LlmRequest;
import com.enterprise.agentic.llmservice.dto.LlmResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class AzureOpenAiClientTest {

    private final AzureOpenAiClient client = new AzureOpenAiClient();

    @Test
    void shouldGenerateResponseSuccessfully() {
        // Given
        LlmRequest request = new LlmRequest("Analyze this log: ERROR: Connection timeout", "gpt-4", 0.7);

        // When
        Mono<LlmResponse> result = client.generate(request);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response != null &&
                        response.response() != null &&
                        !response.response().isEmpty())
                .verifyComplete();
    }

    @Test
    void shouldHandleNullRequest() {
        // When & Then
        assertThrows(NullPointerException.class, () -> client.generate(null));
    }
}
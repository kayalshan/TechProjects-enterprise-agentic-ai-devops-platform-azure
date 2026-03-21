package com.enterprise.agentic.ragservice.client;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class EmbeddingClientTest {

    private final EmbeddingClient client = new EmbeddingClient();

    @Test
    void shouldGenerateEmbeddingSuccessfully() {
        // Given
        String text = "Database connection timeout error";

        // When
        Mono<float[]> result = client.getEmbedding(text);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(embedding ->
                        embedding != null &&
                        embedding.length > 0)
                .verifyComplete();
    }

    @Test
    void shouldHandleEmptyText() {
        // Given
        String text = "";

        // When
        Mono<float[]> result = client.getEmbedding(text);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(embedding -> embedding != null)
                .verifyComplete();
    }

    @Test
    void shouldHandleNullText() {
        // When & Then
        assertThrows(NullPointerException.class, () -> client.getEmbedding(null));
    }
}
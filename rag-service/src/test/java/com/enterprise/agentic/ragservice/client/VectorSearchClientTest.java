package com.enterprise.agentic.ragservice.client;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class VectorSearchClientTest {

    private final VectorSearchClient client = new VectorSearchClient();

    @Test
    void shouldPerformVectorSearchSuccessfully() {
        // Given
        float[] embedding = new float[]{0.1f, 0.2f, 0.3f};

        // When
        Mono<String> result = client.search(embedding);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(context ->
                        context != null &&
                        !context.isEmpty())
                .verifyComplete();
    }

    @Test
    void shouldHandleEmptyEmbedding() {
        // Given
        float[] embedding = new float[0];

        // When
        Mono<String> result = client.search(embedding);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(context -> context != null)
                .verifyComplete();
    }

    @Test
    void shouldHandleNullEmbedding() {
        // When & Then
        assertThrows(NullPointerException.class, () -> client.search(null));
    }
}
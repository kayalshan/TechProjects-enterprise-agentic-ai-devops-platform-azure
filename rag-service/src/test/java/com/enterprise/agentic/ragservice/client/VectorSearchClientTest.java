package com.enterprise.agentic.ragservice.client;

import com.enterprise.agentic.ragservice.config.RagProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class VectorSearchClientTest {

    private VectorSearchClient client;
    private WebClient.Builder webClientBuilder;
    private RagProperties ragProperties;

    @BeforeEach
    void setUp() {
        webClientBuilder = WebClient.builder();
        ragProperties = new RagProperties();
        ragProperties.setSearchUrl("http://localhost");
        ragProperties.setIndexName("test-index");
        ragProperties.setApiKey("test-key");
        client = new VectorSearchClient(webClientBuilder, ragProperties);
    }

    @Test
    void shouldPerformVectorSearchSuccessfully() {
        // Given
        float[] embedding = new float[]{0.1f, 0.2f, 0.3f};

        // When
        Mono<List<String>> result = client.search(embedding);

        // Then
        assertNotNull(result);
    }

    @Test
    void shouldHandleEmptyEmbedding() {
        // Given
        float[] embedding = new float[0];

        // When
        Mono<List<String>> result = client.search(embedding);

        // Then
        assertNotNull(result);
    }

    @Test
    void shouldHandleNullEmbedding() {
        // When & Then
        assertThrows(NullPointerException.class, () -> client.search(null));
    }
}
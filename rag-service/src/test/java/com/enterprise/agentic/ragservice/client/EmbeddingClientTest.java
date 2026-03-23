package com.enterprise.agentic.ragservice.client;

import com.enterprise.agentic.ragservice.config.RagProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmbeddingClientTest {

    private EmbeddingClient client;
    private WebClient.Builder webClientBuilder;
    private RagProperties ragProperties;

    @BeforeEach
    void setUp() {
        webClientBuilder = WebClient.builder();
        ragProperties = new RagProperties();
        ragProperties.setEmbeddingUrl("http://localhost");
        ragProperties.setEmbeddingDeployment("test-deployment");
        client = new EmbeddingClient(webClientBuilder, ragProperties);
    }

    @Test
    void shouldGenerateEmbeddingSuccessfully() {
        // Given
        String text = "Database connection timeout error";

        // When
        Mono<float[]> result = client.getEmbedding(text);

        // Then
        assertNotNull(result);
    }

    @Test
    void shouldHandleEmptyText() {
        // Given
        String text = "";

        // When
        Mono<float[]> result = client.getEmbedding(text);

        // Then
        assertNotNull(result);
    }

    @Test
    void shouldHandleNullText() {
        // When & Then
        assertThrows(NullPointerException.class, () -> client.getEmbedding(null));
    }
}
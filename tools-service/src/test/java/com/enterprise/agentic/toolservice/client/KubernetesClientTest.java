package com.enterprise.agentic.toolservice.client;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class KubernetesClientTest {

    private final KubernetesClient client = new KubernetesClient();

    @Test
    void shouldRestartServiceSuccessfully() {
        // When
        Mono<Void> result = client.restart("test-service");

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void shouldScaleServiceSuccessfully() {
        // When
        Mono<Void> result = client.scale("test-service", 3);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }
}
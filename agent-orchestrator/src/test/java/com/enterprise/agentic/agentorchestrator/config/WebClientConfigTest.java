package com.enterprise.agentic.agentorchestrator.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WebClientConfigTest {

    @Test
    void shouldCreateWebClientBean() {
        WebClientConfig webClientConfig = new WebClientConfig();

        WebClient client = webClientConfig.webClient();

        assertNotNull(client);
        assertNotNull(client.mutate());
    }
}

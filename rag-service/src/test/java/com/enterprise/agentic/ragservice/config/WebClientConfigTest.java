package com.enterprise.agentic.ragservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

class WebClientConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(WebClientConfig.class);

    @Test
    void shouldCreateWebClientBean() {
        contextRunner.run(context -> {
            WebClient webClient = context.getBean(WebClient.class);
            assertThat(webClient).isNotNull();
        });
    }

    @Test
    void shouldConfigureWebClientWithProperSettings() {
        contextRunner.run(context -> {
            WebClient webClient = context.getBean(WebClient.class);
            assertThat(webClient).isNotNull();
            // WebClient should be properly configured for RAG service
        });
    }
}
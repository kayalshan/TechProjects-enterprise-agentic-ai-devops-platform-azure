package com.enterprise.agentic.ragservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class RagPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    @Test
    void shouldLoadDefaultProperties() {
        contextRunner.run(context -> {
            RagProperties properties = context.getBean(RagProperties.class);

            assertThat(properties).isNotNull();
            assertThat(properties.getEmbeddingUrl()).isNotNull();
            assertThat(properties.getSearchUrl()).isNotNull();
        });
    }

    @Test
    void shouldLoadCustomProperties() {
        contextRunner
                .withPropertyValues(
                        "rag.embedding-url=https://custom-embedding.azure.com",
                        "rag.search-url=https://custom-search.azure.com",
                        "rag.api-key=custom-key"
                )
                .run(context -> {
                    RagProperties properties = context.getBean(RagProperties.class);

                    assertThat(properties.getEmbeddingUrl()).isEqualTo("https://custom-embedding.azure.com");
                    assertThat(properties.getSearchUrl()).isEqualTo("https://custom-search.azure.com");
                    assertThat(properties.getApiKey()).isEqualTo("custom-key");
                });
    }

    @EnableConfigurationProperties(RagProperties.class)
    static class TestConfig {
    }
}
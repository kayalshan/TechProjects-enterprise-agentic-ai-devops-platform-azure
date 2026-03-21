package com.enterprise.agentic.llmservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class LlmPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    @Test
    void shouldLoadDefaultProperties() {
        contextRunner.run(context -> {
            LlmProperties properties = context.getBean(LlmProperties.class);

            assertThat(properties).isNotNull();
            assertThat(properties.getDefaultModel()).isNotNull();
            assertThat(properties.getDefaultTemperature()).isNotNull();
        });
    }

    @Test
    void shouldLoadCustomProperties() {
        contextRunner
                .withPropertyValues(
                        "llm.default-model=gpt-4",
                        "llm.default-temperature=0.8",
                        "llm.api-key=test-key",
                        "llm.base-url=https://api.openai.com/v1"
                )
                .run(context -> {
                    LlmProperties properties = context.getBean(LlmProperties.class);

                    assertThat(properties.getDefaultModel()).isEqualTo("gpt-4");
                    assertThat(properties.getDefaultTemperature()).isEqualTo(0.8);
                    assertThat(properties.getApiKey()).isEqualTo("test-key");
                    assertThat(properties.getBaseUrl()).isEqualTo("https://api.openai.com/v1");
                });
    }

    @EnableConfigurationProperties(LlmProperties.class)
    static class TestConfig {
    }
}
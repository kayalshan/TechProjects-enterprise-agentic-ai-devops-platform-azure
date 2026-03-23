package com.enterprise.agentic.llmservice.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LlmPropertiesTest {

    @Test
    void shouldSetAndGetProperties() {
        LlmProperties properties = new LlmProperties();
        properties.setProvider("openai");
        properties.setOpenaiUrl("https://api.openai.com");

        assertThat(properties.getProvider()).isEqualTo("openai");
        assertThat(properties.getOpenaiUrl()).isEqualTo("https://api.openai.com");
    }
}
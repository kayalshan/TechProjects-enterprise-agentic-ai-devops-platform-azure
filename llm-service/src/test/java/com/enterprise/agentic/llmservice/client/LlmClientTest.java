package com.enterprise.agentic.llmservice.client;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LlmClientTest {

    @Test
    void shouldBeMockableAsInterface() {
        LlmClient client = Mockito.mock(LlmClient.class);
        assertNotNull(client);
    }
}
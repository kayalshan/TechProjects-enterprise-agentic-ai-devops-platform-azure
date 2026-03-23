package com.enterprise.agentic.llmservice.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlmProviderTest {

    @Test
    void shouldHaveAllExpectedProviders() {
        LlmProvider[] providers = LlmProvider.values();
        assertArrayEquals(new LlmProvider[]{LlmProvider.OPENAI, LlmProvider.AZURE, LlmProvider.LOCAL}, providers);
    }

    @Test
    void shouldMapProviderNamesFreely() {
        assertEquals(LlmProvider.OPENAI, LlmProvider.from("openai"));
        assertEquals(LlmProvider.AZURE, LlmProvider.from("azure"));
        assertEquals(LlmProvider.AZURE, LlmProvider.from("azure_openai"));
        assertEquals(LlmProvider.LOCAL, LlmProvider.from("local"));
        assertEquals(LlmProvider.OPENAI, LlmProvider.from("unknown"));
        assertEquals(LlmProvider.OPENAI, LlmProvider.from(null));
    }

    @Test
    void shouldHaveValidProviderNames() {
        for (LlmProvider provider : LlmProvider.values()) {
            assertNotNull(provider.name());
            assertFalse(provider.name().isEmpty());
        }
    }
}
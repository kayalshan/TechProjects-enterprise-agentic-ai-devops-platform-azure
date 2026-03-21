package com.enterprise.agentic.llmservice.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlmProviderTest {

    @Test
    void shouldHaveAllExpectedProviders() {
        // When
        LlmProvider[] providers = LlmProvider.values();

        // Then
        assertTrue(providers.length >= 3); // At least OPENAI, AZURE_OPENAI, LOCAL
    }

    @Test
    void shouldFindProviderByName() {
        // When & Then
        assertDoesNotThrow(() -> LlmProvider.valueOf("OPENAI"));
        assertDoesNotThrow(() -> LlmProvider.valueOf("AZURE_OPENAI"));
        assertDoesNotThrow(() -> LlmProvider.valueOf("LOCAL"));
    }

    @Test
    void shouldHaveValidProviderNames() {
        // When
        LlmProvider[] providers = LlmProvider.values();

        // Then
        for (LlmProvider provider : providers) {
            assertNotNull(provider.name());
            assertFalse(provider.name().isEmpty());
        }
    }
}
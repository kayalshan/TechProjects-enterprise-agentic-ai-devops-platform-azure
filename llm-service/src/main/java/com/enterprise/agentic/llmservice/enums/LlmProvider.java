package com.enterprise.agentic.llmservice.enums;

public enum LlmProvider {
    OPENAI,
    AZURE,
    LOCAL;

    public static LlmProvider from(String provider) {
        if (provider == null || provider.isBlank()) {
            return OPENAI;
        }

        return switch (provider.strip().toLowerCase()) {
            case "azure", "azure_openai", "az" -> AZURE;
            case "local", "localhost" -> LOCAL;
            default -> OPENAI;
        };
    }
}
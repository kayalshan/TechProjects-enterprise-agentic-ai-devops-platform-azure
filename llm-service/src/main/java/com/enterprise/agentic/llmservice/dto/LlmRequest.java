package com.enterprise.agentic.llmservice.dto;

import jakarta.validation.constraints.NotBlank;

public record LlmRequest(
        String logMessage,
        String context
) {}
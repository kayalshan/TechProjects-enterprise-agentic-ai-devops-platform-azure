package com.enterprise.agentic.llmservice.dto;

public record RcaResponse(
        String rootCause,
        String impact,
        String severity,
        String suggestedAction,
        String toolName
) {}
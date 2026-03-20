package com.enterprise.agentic.ragservice.dto;

public record RagResponse(
        String answer,
        String source
) {}
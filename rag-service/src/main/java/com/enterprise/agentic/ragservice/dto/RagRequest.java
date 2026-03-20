package com.enterprise.agentic.ragservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RagRequest(
        @NotBlank String query
) {}
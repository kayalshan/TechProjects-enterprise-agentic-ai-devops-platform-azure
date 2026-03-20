package com.enterprise.agentic.agentorchestrator.dto;

import jakarta.validation.constraints.NotBlank;

public record OrchestratorRequest(
        @NotBlank String query
) {}
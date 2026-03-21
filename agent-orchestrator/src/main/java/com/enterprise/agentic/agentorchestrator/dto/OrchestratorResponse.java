package com.enterprise.agentic.agentorchestrator.dto;

public record OrchestratorResponse(
        String rootCause,
        String impact,
        String severity,
        String suggestedAction,
        String toolTriggered,
        String actionResult
) {}
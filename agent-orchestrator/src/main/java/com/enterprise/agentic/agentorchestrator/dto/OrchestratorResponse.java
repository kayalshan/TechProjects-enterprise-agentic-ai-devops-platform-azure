package com.enterprise.agentic.agentorchestrator.dto;

public record OrchestratorResponse(
        String llmAnswer,
        String ragAnswer,
        String toolStatus
) {}
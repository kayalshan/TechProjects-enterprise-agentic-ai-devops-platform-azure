package com.enterprise.agentic.agentorchestrator.dto;

public record ToolExecutionRequest(
        String toolName,
        String serviceName
) {}
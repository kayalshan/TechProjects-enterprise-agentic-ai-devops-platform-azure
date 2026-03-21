package com.enterprise.agentic.toolservice.dto;

public record ToolsRequest(
        String toolName,
        String target,
        String metadata
) {}
package com.enterprise.agentic.agentorchestrator.dto;

import lombok.Data;

@Data
public class LlmResponse {
    private String rootCause;
    private String action;
    private String confidence;
}
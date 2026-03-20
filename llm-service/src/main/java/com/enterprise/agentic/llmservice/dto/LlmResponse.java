package com.enterprise.agentic.llmservice.dto;

import java.time.LocalDateTime;

public record LlmResponse(

        String response,
        String provider,
        String model,
        Long tokensUsed,
        Long latencyMs,
        LocalDateTime timestamp

) {}
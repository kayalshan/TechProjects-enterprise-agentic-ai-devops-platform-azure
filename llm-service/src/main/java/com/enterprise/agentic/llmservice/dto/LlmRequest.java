package com.enterprise.agentic.llmservice.dto;

import jakarta.validation.constraints.NotBlank;

public record LlmRequest(

        @NotBlank(message = "Query cannot be empty")
        String query,

        String context,

        String model,              // gpt-4, gpt-35-turbo, llama3, etc.

        Double temperature,

        String provider           // OPENAI | AZURE | LOCAL

) {}
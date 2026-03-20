package com.enterprise.agentic.llmservice.controller;

import com.enterprise.agentic.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LlmController {

    @GetMapping("/llm/ping")
    public ApiResponse<String> ping() {
        return new ApiResponse<>("LLM Service is up!", "success");
    }
}
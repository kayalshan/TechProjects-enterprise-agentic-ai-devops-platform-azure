package com.enterprise.agentic.llmservice.controller;

import com.enterprise.agentic.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/llm")
public class LlmController {

    private final LlmService service;

    public LlmController(LlmService service) {
        this.service = service;
    }

    @PostMapping("/analyze")
    public Mono<ResponseEntity<RcaResponse>> analyze(@RequestBody LlmRequest request) {

        return service.analyze(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/ping")
    public Mono<String> ping() {
        return Mono.just("LLM Service is up");
    }
}
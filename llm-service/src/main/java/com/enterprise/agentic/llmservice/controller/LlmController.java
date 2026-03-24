package com.enterprise.agentic.llmservice.controller;

import com.enterprise.agentic.llmservice.dto.LlmRequest;
import com.enterprise.agentic.llmservice.dto.RcaResponse;
import com.enterprise.agentic.llmservice.service.LlmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
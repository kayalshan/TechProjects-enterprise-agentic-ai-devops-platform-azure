package com.enterprise.agentic.ragservice.controller;

import com.enterprise.agentic.common.dto.ApiResponse;
import com.enterprise.agentic.ragservice.dto.RagRequest;
import com.enterprise.agentic.ragservice.dto.RagResponse;
import com.enterprise.agentic.ragservice.service.RagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final RagService service;

    public RagController(RagService service) {
        this.service = service;
    }

    @PostMapping("/enrich")
    public Mono<ResponseEntity<RagResponse>> enrich(@RequestBody RagRequest request) {
        return service.enrich(request).map(ResponseEntity::ok);
    }

    @GetMapping("/ping")
    public Mono<String> ping() {
        return Mono.just("RAG Service is up");
    }
}
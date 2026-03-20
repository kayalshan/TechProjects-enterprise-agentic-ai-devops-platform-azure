package com.enterprise.agentic.ragservice.controller;

import com.enterprise.agentic.common.dto.ApiResponse;
import com.enterprise.agentic.ragservice.dto.RagRequest;
import com.enterprise.agentic.ragservice.dto.RagResponse;
import com.enterprise.agentic.ragservice.service.RagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/query")
    public ResponseEntity<ApiResponse<RagResponse>> query(@Valid @RequestBody RagRequest request) {
        RagResponse response = ragService.processQuery(request);
        return ResponseEntity.ok(new ApiResponse<>(response, "success"));
    }

    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping() {
        return ResponseEntity.ok(new ApiResponse<>("RAG Service is up!", "success"));
    }
}
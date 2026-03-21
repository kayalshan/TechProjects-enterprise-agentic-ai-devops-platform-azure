package com.enterprise.agentic.agentorchestrator.controller;

import com.enterprise.agentic.agentorchestrator.dto.OrchestratorRequest;
import com.enterprise.agentic.agentorchestrator.dto.OrchestratorResponse;
import com.enterprise.agentic.agentorchestrator.service.AgentOrchestratorService;
import com.enterprise.agentic.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController {

    private final AgentOrchestratorService service;

    public OrchestratorController(AgentOrchestratorService service) {
        this.service = service;
    }

    @PostMapping("/analyze-log")
    public ResponseEntity<ApiResponse<OrchestratorResponse>> analyze(
            @Valid @RequestBody OrchestratorRequest request) {

        return ResponseEntity.ok(
                new ApiResponse<>(service.process(request), "SUCCESS")
        );
    }

    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping() {
        return ResponseEntity.ok(
                new ApiResponse<>("Orchestrator is UP", "SUCCESS")
        );
    }
}
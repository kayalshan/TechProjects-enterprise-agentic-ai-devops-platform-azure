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

    private final AgentOrchestratorService orchestratorService;

    public OrchestratorController(AgentOrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @PostMapping("/execute-task")
    public ResponseEntity<ApiResponse<OrchestratorResponse>> executeTask(
            @Valid @RequestBody OrchestratorRequest request) {
        OrchestratorResponse response = orchestratorService.executeTask(request);
        return ResponseEntity.ok(new ApiResponse<>(response, "success"));
    }

    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping() {
        return ResponseEntity.ok(new ApiResponse<>("Agent Orchestrator is up!", "success"));
    }
}
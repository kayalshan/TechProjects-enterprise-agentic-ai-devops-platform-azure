package com.enterprise.agentic.toolservice.controller;

import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import com.enterprise.agentic.toolservice.service.ToolsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tools")
public class ToolsController {

    private final ToolsService toolsService;

    public ToolsController(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    @PostMapping("/execute")
    public Mono<ResponseEntity<ToolsResponse>> executeTool(@RequestBody ToolsRequest request) {
        return toolsService.executeTool(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Tools Service is running");
    }
}
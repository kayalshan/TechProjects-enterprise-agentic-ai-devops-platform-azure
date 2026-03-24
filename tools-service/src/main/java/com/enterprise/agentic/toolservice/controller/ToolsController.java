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

    private final ToolsService service;

    public ToolsController(ToolsService service) {
        this.service = service;
    }

    @PostMapping("/execute")
    public Mono<ResponseEntity<ToolsResponse>> execute(@RequestBody ToolsRequest request) {
        return service.execute(request).map(ResponseEntity::ok);
    }

    @GetMapping("/ping")
    public Mono<String> ping() {
        return Mono.just("Tools Service is up");
    }
}
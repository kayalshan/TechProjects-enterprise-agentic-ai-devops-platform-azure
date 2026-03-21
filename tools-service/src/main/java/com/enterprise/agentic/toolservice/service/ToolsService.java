package com.enterprise.agentic.toolservice.service;

import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import com.enterprise.agentic.toolservice.executor.ToolExecutor;
import com.enterprise.agentic.toolservice.executor.ToolExecutorFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ToolsService {

    private final ToolExecutorFactory factory;

    public ToolsService(ToolExecutorFactory factory) {
        this.factory = factory;
    }

    public Mono<ToolsResponse> execute(ToolsRequest request) {

        ToolExecutor executor = factory.getExecutor(request.toolName());

        return executor.execute(request);
    }
}

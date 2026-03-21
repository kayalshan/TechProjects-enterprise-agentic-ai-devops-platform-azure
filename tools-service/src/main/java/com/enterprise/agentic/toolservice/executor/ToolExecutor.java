package com.enterprise.agentic.toolservice.executor;

import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import reactor.core.publisher.Mono;

public interface ToolExecutor {

    String getName();

    Mono<ToolsResponse> execute(ToolsRequest request);
}
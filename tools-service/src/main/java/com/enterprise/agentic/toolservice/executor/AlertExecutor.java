package com.enterprise.agentic.toolservice.executor;

import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AlertExecutor implements ToolExecutor {

    @Override
    public String getName() {
        return "send-alert";
    }

    @Override
    public Mono<ToolsResponse> execute(ToolsRequest request) {

        // simulate alert
        return Mono.just(new ToolsResponse("SUCCESS", "Alert sent for: " + request.target()));
    }
}
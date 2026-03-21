package com.enterprise.agentic.toolservice.executor;

import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("send-alert")
public class AlertExecutor implements ToolExecutor {

    @Override
    public Mono<ToolsResponse> execute(ToolsRequest request) {
        // Simulate sending alert - in real implementation, integrate with Slack, Email, PagerDuty, etc.
        return Mono.just(new ToolsResponse("SUCCESS", "Alert sent for: " + request.target()));
    }
}
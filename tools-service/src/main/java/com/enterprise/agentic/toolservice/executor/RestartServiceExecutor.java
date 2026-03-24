package com.enterprise.agentic.toolservice.executor;

import com.enterprise.agentic.toolservice.client.KubernetesClient;
import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RestartServiceExecutor implements ToolExecutor {

    private final KubernetesClient client;

    public RestartServiceExecutor(KubernetesClient client) {
        this.client = client;
    }

    @Override
    public String getName() {
        return "restart-service";
    }

    @Override
    public Mono<ToolsResponse> execute(ToolsRequest request) {

        return client.restart(request.target())
                .then(Mono.just(new ToolsResponse("SUCCESS", "Service restarted: " + request.target())))
                .onErrorResume(ex -> Mono.just(new ToolsResponse("FAILED", "Failed to restart service: " + ex.getMessage())));
    }
}
package com.enterprise.agentic.toolservice.executor;

import com.enterprise.agentic.toolservice.client.KubernetesClient;
import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("restart-service")
public class RestartServiceExecutor implements ToolExecutor {

    private final KubernetesClient kubernetesClient;

    public RestartServiceExecutor(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public Mono<ToolsResponse> execute(ToolsRequest request) {
        return kubernetesClient.restartService(request.target())
                .map(result -> new ToolsResponse("SUCCESS", "Service restarted: " + request.target()))
                .onErrorResume(e -> Mono.just(new ToolsResponse("FAILED", "Failed to restart service: " + e.getMessage())));
    }
}
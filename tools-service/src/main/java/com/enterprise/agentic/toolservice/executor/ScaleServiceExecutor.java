package com.enterprise.agentic.toolservice.executor;

import com.enterprise.agentic.toolservice.client.KubernetesClient;
import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("scale-service")
public class ScaleServiceExecutor implements ToolExecutor {

    private final KubernetesClient kubernetesClient;

    public ScaleServiceExecutor(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public Mono<ToolsResponse> execute(ToolsRequest request) {
        return kubernetesClient.scaleService(request.target(), request.metadata())
                .map(result -> new ToolsResponse("SUCCESS", "Service scaled: " + request.target()))
                .onErrorResume(e -> Mono.just(new ToolsResponse("FAILED", "Failed to scale service: " + e.getMessage())));
    }
}
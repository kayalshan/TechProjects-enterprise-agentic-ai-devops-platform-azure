package com.enterprise.agentic.toolservice.executor;

import com.enterprise.agentic.toolservice.client.KubernetesClient;
import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ScaleServiceExecutor implements ToolExecutor {

    private final KubernetesClient client;

    public ScaleServiceExecutor(KubernetesClient client) {
        this.client = client;
    }

    @Override
    public String getName() {
        return "scale-service";
    }

    @Override
    public Mono<ToolsResponse> execute(ToolsRequest request) {
        try {
            int replicas = Integer.parseInt(request.metadata());
            return client.scale(request.target(), replicas)
                    .then(Mono.just(new ToolsResponse("SUCCESS", "Scaled service: " + request.target() + " to " + replicas + " replicas")));
        } catch (NumberFormatException e) {
            return Mono.just(new ToolsResponse("FAILED", "Invalid replica count: " + request.metadata()));
        }
    }
}
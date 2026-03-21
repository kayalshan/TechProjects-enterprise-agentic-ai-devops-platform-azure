package com.enterprise.agentic.toolservice.client;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
public class KubernetesClient {

    public Mono<Void> restart(String serviceName) {
        System.out.println("Restarting: " + serviceName);
        return Mono.empty();
    }

    public Mono<Void> scale(String serviceName, int replicas) {
        System.out.println("Scaling: " + serviceName + " to " + replicas);
        return Mono.empty();
    }
}
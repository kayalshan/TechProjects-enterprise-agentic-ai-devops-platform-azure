package com.enterprise.agentic.agentorchestrator.client;

import com.enterprise.agentic.agentorchestrator.config.OrchestratorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ToolsClient {

    private final WebClient webClient;
    private final OrchestratorProperties properties;

    public Mono<String> executeAction(String action, String parameters) {
        log.info("Calling Tools service to execute action: {}", action);

        Map<String, String> request = Map.of(
            "action", action,
            "parameters", parameters
        );

        return webClient.post()
                .uri(properties.getToolsServiceUrl() + "/api/tools/execute")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Action executed successfully by Tools service"))
                .doOnError(error -> log.error("Error calling Tools service", error));
    }
}
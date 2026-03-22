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
public class RagClient {

    private final WebClient webClient;
    private final OrchestratorProperties properties;

    public Mono<String> getContext(String query) {
        log.info("Calling RAG service for context with query: {}", query);

        return webClient.post()
                .uri(properties.getRagServiceUrl() + "/api/rag/context")
                .bodyValue(Map.of("query", query))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Received context from RAG service"))
                .doOnError(error -> log.error("Error calling RAG service", error));
    }
}
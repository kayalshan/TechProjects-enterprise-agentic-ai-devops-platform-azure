package com.enterprise.agentic.agentorchestrator.client;

import com.enterprise.agentic.agentorchestrator.config.OrchestratorProperties;
import com.enterprise.agentic.agentorchestrator.dto.LlmResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class LlmClient {

    private final WebClient webClient;
    private final OrchestratorProperties properties;

    public Mono<LlmResponse> analyzeLog(String logData, String context) {
        log.info("Calling LLM service for log analysis");

        Map<String, String> request = Map.of(
            "logData", logData,
            "context", context
        );

        return webClient.post()
                .uri(properties.getLlmServiceUrl() + "/api/llm/analyze")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LlmResponse.class)
                .doOnSuccess(response -> log.info("Received analysis from LLM service"))
                .doOnError(error -> log.error("Error calling LLM service", error));
    }
}
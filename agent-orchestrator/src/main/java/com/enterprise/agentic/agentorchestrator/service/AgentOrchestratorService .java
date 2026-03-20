package com.enterprise.agentic.agentorchestrator.service;

import com.enterprise.agentic.agentorchestrator.dto.OrchestratorRequest;
import com.enterprise.agentic.agentorchestrator.dto.OrchestratorResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AgentOrchestratorService {

    private final WebClient webClient = WebClient.builder().build();

    public OrchestratorResponse executeTask(OrchestratorRequest request) {
        String query = request.query();

        // Call LLM service
        String llmAnswer = webClient.post()
                .uri("http://localhost:8081/llm/query")
                .bodyValue("{\"query\":\"" + query + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Call RAG service
        String ragAnswer = webClient.post()
                .uri("http://localhost:8082/rag/query")
                .bodyValue("{\"query\":\"" + query + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Call Tools service
        String toolStatus = webClient.get()
                .uri("http://localhost:8083/tools/status")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return new OrchestratorResponse(llmAnswer, ragAnswer, toolStatus);
    }
}
package com.enterprise.agentic.llmservice.service.client;

import com.enterprise.agentic.llmservice.dto.LlmRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AzureOpenAiClient {

    @Value("${azure.api-key}")
    private String apiKey;

    @Value("${azure.endpoint}")
    private String endpoint;

    @Value("${azure.deployment}")
    private String deployment;

    private final WebClient webClient = WebClient.builder().build();

    public String call(String prompt, LlmRequest request) {

        String url = endpoint + "/openai/deployments/" + deployment + "/chat/completions?api-version=2024-02-15-preview";

        String body = """
        {
          "messages": [{"role": "user", "content": "%s"}],
          "temperature": %s
        }
        """.formatted(prompt,
                request.temperature() != null ? request.temperature() : 0.7);

        return webClient.post()
                .uri(url)
                .header("api-key", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
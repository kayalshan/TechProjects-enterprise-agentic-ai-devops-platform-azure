package com.enterprise.agentic.llmservice.service.client;

import com.enterprise.agentic.llmservice.dto.LlmRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenAiClient {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.url}")
    private String url;

    private final WebClient webClient = WebClient.builder().build();

    public String call(String prompt, LlmRequest request) {

        String body = """
        {
          "model": "%s",
          "messages": [{"role": "user", "content": "%s"}],
          "temperature": %s
        }
        """.formatted(
                request.model() != null ? request.model() : "gpt-4",
                prompt,
                request.temperature() != null ? request.temperature() : 0.7
        );

        return webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
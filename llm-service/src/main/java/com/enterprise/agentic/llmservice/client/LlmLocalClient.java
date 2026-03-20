package com.enterprise.agentic.llmservice.service.client;

import com.enterprise.agentic.llmservice.dto.LlmRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class LocalLlmClient {

    @Value("${local.url}")
    private String url;

    @Value("${local.model}")
    private String model;

    private final WebClient webClient = WebClient.builder().build();

    public String call(String prompt, LlmRequest request) {

        String body = """
        {
          "model": "%s",
          "prompt": "%s",
          "stream": false
        }
        """.formatted(model, prompt);

        return webClient.post()
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

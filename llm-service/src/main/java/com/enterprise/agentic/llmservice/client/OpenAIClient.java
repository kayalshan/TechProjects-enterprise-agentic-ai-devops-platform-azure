package com.enterprise.agentic.llmservice.client;

import com.enterprise.agentic.llmservice.dto.LlmRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenAiClient implements LlmClient {

    private final WebClient webClient;

    public OpenAiClient(WebClient.Builder builder, LlmProperties props) {
        this.webClient = builder.baseUrl(props.getOpenaiUrl()).build();
    }

    @Override
    public Mono<String> call(String prompt) {

        return webClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer YOUR_KEY")
                .bodyValue(Map.of(
                        "model", "gpt-4o-mini",
                        "messages", List.of(Map.of("role", "user", "content", prompt))
                ))
                .retrieve()
                .bodyToMono(String.class);
    }
}
package com.enterprise.agentic.llmservice.client;

import com.enterprise.agentic.llmservice.config.LlmProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class LocalLlmClient implements LlmClient {

    private final WebClient webClient;
    private final LlmProperties props;

    public LocalLlmClient(WebClient.Builder builder, LlmProperties props) {
        this.props = props;
        this.webClient = builder.baseUrl(props.getLocalUrl()).build();
    }

    @Override
    public Mono<String> call(String prompt) {

        return webClient.post()
                .uri("/api/generate")
                .bodyValue(Map.of(
                        "model", "llama3",
                        "prompt", prompt,
                        "stream", false
                ))
                .retrieve()
                .bodyToMono(String.class)
                .map(resp -> extractResponse(resp));
    }

    private String extractResponse(String raw) {
        // simple extraction (Ollama returns JSON)
        int start = raw.indexOf("\"response\":\"") + 12;
        int end = raw.lastIndexOf("\"");
        return raw.substring(start, end);
    }
}
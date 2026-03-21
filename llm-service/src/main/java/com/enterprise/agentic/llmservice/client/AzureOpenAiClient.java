package com.enterprise.agentic.llmservice.client;

import com.enterprise.agentic.llmservice.config.LlmProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.List;

@Component
public class AzureOpenAiClient implements LlmClient {

    private final WebClient webClient;
    private final LlmProperties props;

    public AzureOpenAiClient(WebClient.Builder builder, LlmProperties props) {
        this.props = props;
        this.webClient = builder.baseUrl(props.getAzureUrl()).build();
    }

    @Override
    public Mono<String> call(String prompt) {

        return webClient.post()
                .uri("/openai/deployments/" + props.getAzureDeployment()
                        + "/chat/completions?api-version=2024-02-15-preview")
                .header("api-key", props.getAzureApiKey())
                .bodyValue(Map.of(
                        "messages", List.of(
                                Map.of("role", "user", "content", prompt)
                        )
                ))
                .retrieve()
                .bodyToMono(String.class);
    }
}
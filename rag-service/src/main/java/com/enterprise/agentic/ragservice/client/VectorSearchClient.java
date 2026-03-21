package com.enterprise.agentic.ragservice.client;

import com.enterprise.agentic.ragservice.config.RagProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class VectorSearchClient {

    private final WebClient webClient;
    private final RagProperties props;

    public VectorSearchClient(WebClient.Builder builder, RagProperties props) {
        this.props = props;
        this.webClient = builder.baseUrl(props.getSearchUrl()).build();
    }

    public Mono<List<String>> search(float[] vector) {

        return webClient.post()
                .uri("/indexes/" + props.getIndexName() + "/docs/search?api-version=2023-11-01")
                .header("api-key", props.getApiKey())
                .bodyValue(buildRequest(vector))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(this::extractContexts);
    }

    private Map<String, Object> buildRequest(float[] vector) {
        return Map.of(
                "vector", Map.of(
                        "value", vector,
                        "k", 3,
                        "fields", "embedding"
                )
        );
    }

    private List<String> extractContexts(JsonNode json) {
        if (json == null || !json.has("value")) {
            return List.of("No relevant context found");
        }

        JsonNode docs = json.path("value");
        if (!docs.isArray()) {
            return List.of("No relevant context found");
        }

        List<String> results = new ArrayList<>();

        for (JsonNode doc : docs) {
            if (doc.has("content")) {
                String context = doc.path("content").asText();
                if (context != null && !context.trim().isEmpty()) {
                    results.add(context);
                }
            }
        }

        return results.isEmpty()
                ? List.of("No relevant context found")
                : results;
    }
}
package com.enterprise.agentic.ragservice.client;

import com.enterprise.agentic.ragservice.config.RagProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class EmbeddingClient {

    private final WebClient webClient;
    private final RagProperties props;

    public EmbeddingClient(WebClient.Builder builder, RagProperties props) {
        this.props = props;
        this.webClient = builder.baseUrl(props.getEmbeddingUrl()).build();
    }

    public Mono<float[]> getEmbedding(String text) {

        return webClient.post()
                .uri("/openai/deployments/" + props.getEmbeddingDeployment()
                        + "/embeddings?api-version=2024-02-15-preview")
                .header("api-key", props.getApiKey())
                .bodyValue(Map.of("input", text))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(this::extractEmbedding);
    }

    private float[] extractEmbedding(JsonNode json) {
        if (json == null || !json.has("data") || json.path("data").isEmpty()) {
            throw new RuntimeException("Invalid embedding response: missing or empty data");
        }

        JsonNode data = json.path("data");
        if (!data.isArray() || data.size() == 0) {
            throw new RuntimeException("Invalid embedding response: data is not an array or is empty");
        }

        JsonNode firstItem = data.get(0);
        if (!firstItem.has("embedding")) {
            throw new RuntimeException("Invalid embedding response: missing embedding field");
        }

        JsonNode embeddingNode = firstItem.path("embedding");
        if (!embeddingNode.isArray()) {
            throw new RuntimeException("Invalid embedding response: embedding is not an array");
        }

        float[] vector = new float[embeddingNode.size()];

        for (int i = 0; i < embeddingNode.size(); i++) {
            vector[i] = (float) embeddingNode.get(i).asDouble();
        }

        return vector;
    }
}
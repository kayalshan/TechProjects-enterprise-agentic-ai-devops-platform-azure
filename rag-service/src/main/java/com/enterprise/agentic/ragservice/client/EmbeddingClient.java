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

        JsonNode embeddingNode = json.path("data").get(0).path("embedding");

        float[] vector = new float[embeddingNode.size()];

        for (int i = 0; i < embeddingNode.size(); i++) {
            vector[i] = (float) embeddingNode.get(i).asDouble();
        }

        return vector;
    }
}
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

        List<String> results = new ArrayList<>();

        JsonNode docs = json.path("value");

        for (JsonNode doc : docs) {
            String context = doc.path("content").asText();
            if (!context.isEmpty()) {
                results.add(context);
            }
        }

        return results.isEmpty()
                ? List.of("No relevant context found")
                : results;
    }
}
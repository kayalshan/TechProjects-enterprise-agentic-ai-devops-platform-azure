@Repository
public class InMemoryRagRepository {

    private final Map<String, String> data = Map.of(
            "db timeout", "Database overload during peak traffic",
            "500 error", "Downstream service failure",
            "connection refused", "Service may be down"
    );

    public String findRelevantContext(String log) {

        return data.entrySet().stream()
                .filter(e -> log.toLowerCase().contains(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("No context found");
    }
}
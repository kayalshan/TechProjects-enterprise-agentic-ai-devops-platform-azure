package com.enterprise.agentic.ragservice.repository;

import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Map;

@Repository
public class InMemoryRagRepository {

    private static final Map<String, String> data = Map.of(
            "db timeout", "Database overload during peak traffic",
            "500 error", "Downstream service failure",
            "connection refused", "Service may be down"
    );

    public String findRelevantContext(String log) {
        if (log == null || log.isBlank()) {
            return "No context found";
        }

        String normalizedLog = log.toLowerCase(Locale.ROOT);

        return data.entrySet().stream()
                .filter(e -> normalizedLog.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("No context found");
    }
}
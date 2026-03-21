package com.enterprise.agentic.ragservice.service;

import com.enterprise.agentic.ragservice.dto.RagRequest;
import com.enterprise.agentic.ragservice.dto.RagResponse;
import org.springframework.stereotype.Service;

@Service
public class RagService {

    private final EmbeddingClient embeddingClient;
    private final VectorSearchClient vectorClient;
    private final InMemoryRagRepository fallbackRepo;

    public RagService(EmbeddingClient embeddingClient,
                      VectorSearchClient vectorClient,
                      InMemoryRagRepository fallbackRepo) {
        this.embeddingClient = embeddingClient;
        this.vectorClient = vectorClient;
        this.fallbackRepo = fallbackRepo;
    }

    public Mono<RagResponse> enrich(RagRequest request) {

        String log = request.logMessage();

        return embeddingClient.getEmbedding(log)

                .flatMap(vectorClient::search)

                .map(context -> new RagResponse(context))

                //  fallback if vector fails
                .onErrorResume(ex -> {
                    String fallback = fallbackRepo.findRelevantContext(log);
                    return Mono.just(new RagResponse(fallback));
                });
    }
}
package com.enterprise.agentic.ragservice.service;

import com.enterprise.agentic.ragservice.client.EmbeddingClient;
import com.enterprise.agentic.ragservice.client.VectorSearchClient;
import com.enterprise.agentic.ragservice.dto.RagRequest;
import com.enterprise.agentic.ragservice.repository.InMemoryRagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RagServiceTest {

    @Test
    void shouldUseFallbackWhenVectorFails() {

        EmbeddingClient embedding = mock(EmbeddingClient.class);
        VectorSearchClient vector = mock(VectorSearchClient.class);
        InMemoryRagRepository repo = new InMemoryRagRepository();

        when(embedding.getEmbedding(any()))
                .thenReturn(Mono.just(new float[]{1f}));

        when(vector.search(any()))
                .thenReturn(Mono.error(new RuntimeException()));

        RagService service = new RagService(embedding, vector, repo);

        StepVerifier.create(service.enrich(new RagRequest("db timeout")))
                .expectNextMatches(r -> r.context().contains("Database"))
                .verifyComplete();
    }

    @Test
    void shouldReturnMatchingContextFromFallbackRepository() {
        InMemoryRagRepository repo = new InMemoryRagRepository();

        String context = repo.findRelevantContext("Error: connection refused by API");

        Assertions.assertEquals("Service may be down", context);
    }

    @Test
    void shouldReturnNoContextForUnknownLog() {
        InMemoryRagRepository repo = new InMemoryRagRepository();

        String context = repo.findRelevantContext("some random log text");

        Assertions.assertEquals("No context found", context);
    }

    @Test
    void shouldBeCaseInsensitiveAndHandleBlankOrNull() {
        InMemoryRagRepository repo = new InMemoryRagRepository();

        Assertions.assertEquals("Database overload during peak traffic", repo.findRelevantContext("DB TIMEOUT"));
        Assertions.assertEquals("No context found", repo.findRelevantContext(""));
        Assertions.assertEquals("No context found", repo.findRelevantContext(null));
    }
}
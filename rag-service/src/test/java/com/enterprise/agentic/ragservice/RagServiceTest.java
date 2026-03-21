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
}
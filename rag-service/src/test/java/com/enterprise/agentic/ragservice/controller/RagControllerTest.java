package com.enterprise.agentic.ragservice.controller;

import com.enterprise.agentic.ragservice.dto.RagRequest;
import com.enterprise.agentic.ragservice.dto.RagResponse;
import com.enterprise.agentic.ragservice.service.RagService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RagControllerTest {

    private final RagService ragService = mock(RagService.class);
    private final RagController controller = new RagController(ragService);

    @Test
    void shouldEnrichLogSuccessfully() {
        // Given
        RagRequest request = new RagRequest("ERROR: DB timeout");
        RagResponse response = new RagResponse("Database overload during peak traffic");
        when(ragService.enrich(any(RagRequest.class))).thenReturn(Mono.just(response));

        // When
        Mono<ResponseEntity<RagResponse>> result = controller.enrich(request);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(entity ->
                        entity.getStatusCode().is2xxSuccessful() &&
                        entity.getBody().equals(response))
                .verifyComplete();
    }

    @Test
    void shouldHandleServiceFailure() {
        // Given
        RagRequest request = new RagRequest("ERROR: Unknown issue");
        RuntimeException exception = new RuntimeException("Service error");
        when(ragService.enrich(any(RagRequest.class))).thenReturn(Mono.error(exception));

        // When
        Mono<ResponseEntity<RagResponse>> result = controller.enrich(request);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldReturnHealthCheckResponse() {
        // When
        String result = controller.ping();

        // Then
        assert(result.equals("RAG Service is running"));
    }
}
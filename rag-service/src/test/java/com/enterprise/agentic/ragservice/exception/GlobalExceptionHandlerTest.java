package com.enterprise.agentic.ragservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleRagServiceException() {
        // Given
        RagServiceException exception = new RagServiceException("RAG service error");

        // When
        Mono<ResponseEntity<String>> result = handler.handleRagServiceException(exception);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(entity ->
                        entity.getStatusCode() == HttpStatus.BAD_REQUEST &&
                        entity.getBody().equals("RAG service error"))
                .verifyComplete();
    }

    @Test
    void shouldHandleGenericException() {
        // Given
        RuntimeException exception = new RuntimeException("Unexpected error");

        // When
        Mono<ResponseEntity<Map<String, Object>>> result = handler.handleGenericException(exception);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(entity -> {
                    Map<String, Object> body = entity.getBody();
                    return entity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR &&
                           "Unexpected error".equals(body.get("error")) &&
                           body.get("status").equals(500);
                })
                .verifyComplete();
    }
}
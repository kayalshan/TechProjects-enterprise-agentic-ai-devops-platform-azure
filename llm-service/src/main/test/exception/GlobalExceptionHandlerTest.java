package com.enterprise.agentic.llmservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleLlmException() {

        LlmException ex = new LlmException("TEST_ERROR", "Failure");

        Mono<ResponseEntity<java.util.Map<String, Object>>> response =
                handler.handleLlmException(ex);

        StepVerifier.create(response)
                .expectNextMatches(res ->
                        res.getStatusCode().is5xxServerError() &&
                        res.getBody().get("errorCode").equals("TEST_ERROR")
                )
                .verifyComplete();
    }
}

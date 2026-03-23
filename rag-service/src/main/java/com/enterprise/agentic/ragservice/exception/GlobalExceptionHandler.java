package com.enterprise.agentic.ragservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RagServiceException.class)
    public Mono<ResponseEntity<String>> handleRagServiceException(RagServiceException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(Exception ex) {
        return Mono.just(
                ResponseEntity.status(500)
                        .body(Map.of(
                                "error", ex.getMessage(),
                                "status", 500
                        ))
        );
    }
}
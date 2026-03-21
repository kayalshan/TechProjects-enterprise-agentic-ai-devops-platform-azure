package com.enterprise.agentic.llmservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ✅ Handle LLM Exception
    @ExceptionHandler(LlmException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleLlmException(LlmException ex) {

        log.error("LLM Exception: {}", ex.getMessage(), ex);

        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(buildResponse(
                                ex.getErrorCode(),
                                ex.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR
                        ))
        );
    }

    // ✅ Handle Illegal Arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleBadRequest(IllegalArgumentException ex) {

        log.warn("Bad Request: {}", ex.getMessage());

        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(buildResponse(
                                "BAD_REQUEST",
                                ex.getMessage(),
                                HttpStatus.BAD_REQUEST
                        ))
        );
    }

    // ✅ Handle All Other Exceptions
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGeneric(Exception ex) {

        log.error("Unhandled Exception: {}", ex.getMessage(), ex);

        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(buildResponse(
                                "INTERNAL_ERROR",
                                "Something went wrong",
                                HttpStatus.INTERNAL_SERVER_ERROR
                        ))
        );
    }

    // ✅ Standard Response Builder
    private Map<String, Object> buildResponse(String code, String message, HttpStatus status) {

        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "errorCode", code,
                "message", message
        );
    }
}
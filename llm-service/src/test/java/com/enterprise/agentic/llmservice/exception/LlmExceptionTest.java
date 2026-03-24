package com.enterprise.agentic.llmservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlmExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultCodeAndMessage() {
        // Given
        String message = "LLM service error";

        // When
        LlmException exception = new LlmException(message);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals("LLM_ERROR", exception.getErrorCode());
    }

    @Test
    void shouldCreateExceptionWithCodeAndMessageAndCause() {
        // Given
        String errorCode = "INVALID_INPUT";
        String message = "LLM service error";
        Throwable cause = new RuntimeException("Root cause");

        // When
        LlmException exception = new LlmException(errorCode, message, cause);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldBeRuntimeException() {
        LlmException exception = new LlmException("Test");
        assertTrue(exception instanceof RuntimeException);
    }
}
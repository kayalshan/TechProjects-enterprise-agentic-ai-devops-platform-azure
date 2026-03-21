package com.enterprise.agentic.llmservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlmExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        // Given
        String message = "LLM service error";

        // When
        LlmException exception = new LlmException(message);

        // Then
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        // Given
        String message = "LLM service error";
        Throwable cause = new RuntimeException("Root cause");

        // When
        LlmException exception = new LlmException(message, cause);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldBeRuntimeException() {
        // Given
        LlmException exception = new LlmException("Test");

        // Then
        assertTrue(exception instanceof RuntimeException);
    }
}
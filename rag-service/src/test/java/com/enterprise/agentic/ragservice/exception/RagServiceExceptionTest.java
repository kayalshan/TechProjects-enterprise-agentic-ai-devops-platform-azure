package com.enterprise.agentic.ragservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RagServiceExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        // Given
        String message = "RAG service error";

        // When
        RagServiceException exception = new RagServiceException(message);

        // Then
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        // Given
        String message = "RAG service error";
        Throwable cause = new RuntimeException("Root cause");

        // When
        RagServiceException exception = new RagServiceException(message, cause);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldBeRuntimeException() {
        // Given
        RagServiceException exception = new RagServiceException("Test");

        // Then
        assertTrue(exception instanceof RuntimeException);
    }
}
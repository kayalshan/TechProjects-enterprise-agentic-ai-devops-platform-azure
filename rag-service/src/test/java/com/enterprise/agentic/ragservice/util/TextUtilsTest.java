package com.enterprise.agentic.ragservice.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextUtilsTest {

    @Test
    void shouldCleanTextSuccessfully() {
        // Given
        String dirtyText = "  ERROR: Database timeout\n\twith extra spaces  ";

        // When
        String result = TextUtils.cleanText(dirtyText);

        // Then
        assertNotNull(result);
        assertFalse(result.contains("\n"));
        assertFalse(result.contains("\t"));
        assertTrue(result.trim().equals(result));
    }

    @Test
    void shouldHandleNullText() {
        // When
        String result = TextUtils.cleanText(null);

        // Then
        assertNull(result);
    }

    @Test
    void shouldHandleEmptyText() {
        // When
        String result = TextUtils.cleanText("");

        // Then
        assertEquals("", result);
    }

    @Test
    void shouldNormalizeWhitespace() {
        // Given
        String text = "Multiple   spaces\tand\nnewlines";

        // When
        String result = TextUtils.cleanText(text);

        // Then
        assertFalse(result.contains("  ")); // No double spaces
        assertFalse(result.contains("\t"));
        assertFalse(result.contains("\n"));
    }
}
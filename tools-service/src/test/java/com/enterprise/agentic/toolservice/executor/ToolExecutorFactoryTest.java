package com.enterprise.agentic.toolservice.executor;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToolExecutorFactoryTest {

    @Test
    void shouldCreateFactoryWithExecutors() {
        // Given
        List<ToolExecutor> executors = List.of(
                new RestartServiceExecutor(null),
                new ScaleServiceExecutor(null),
                new AlertExecutor()
        );

        // When
        ToolExecutorFactory factory = new ToolExecutorFactory(executors);

        // Then
        assertNotNull(factory);
        assertDoesNotThrow(() -> factory.getExecutor("restart-service"));
        assertDoesNotThrow(() -> factory.getExecutor("scale-service"));
        assertDoesNotThrow(() -> factory.getExecutor("send-alert"));
    }

    @Test
    void shouldReturnCorrectExecutorByName() {
        // Given
        RestartServiceExecutor restartExecutor = new RestartServiceExecutor(null);
        ScaleServiceExecutor scaleExecutor = new ScaleServiceExecutor(null);
        AlertExecutor alertExecutor = new AlertExecutor();

        List<ToolExecutor> executors = List.of(restartExecutor, scaleExecutor, alertExecutor);
        ToolExecutorFactory factory = new ToolExecutorFactory(executors);

        // When & Then
        assertEquals(restartExecutor, factory.getExecutor("restart-service"));
        assertEquals(scaleExecutor, factory.getExecutor("scale-service"));
        assertEquals(alertExecutor, factory.getExecutor("send-alert"));
    }

    @Test
    void shouldThrowExceptionForUnknownTool() {
        // Given
        List<ToolExecutor> executors = List.of(new AlertExecutor());
        ToolExecutorFactory factory = new ToolExecutorFactory(executors);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> factory.getExecutor("unknown-tool"));
        assertEquals("Unknown tool: unknown-tool", exception.getMessage());
    }
}
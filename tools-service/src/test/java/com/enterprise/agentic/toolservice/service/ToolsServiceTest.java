package com.enterprise.agentic.toolservice.service;

import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import com.enterprise.agentic.toolservice.executor.ToolExecutor;
import com.enterprise.agentic.toolservice.executor.ToolExecutorFactory;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ToolsServiceTest {

    private final ToolExecutorFactory factory = mock(ToolExecutorFactory.class);
    private final ToolExecutor executor = mock(ToolExecutor.class);
    private final ToolsService service = new ToolsService(factory);

    @Test
    void shouldExecuteToolSuccessfully() {
        // Given
        ToolsRequest request = new ToolsRequest("restart-service", "payment-service", null);
        ToolsResponse expectedResponse = new ToolsResponse("SUCCESS", "Service restarted");

        when(factory.getExecutor(anyString())).thenReturn(executor);
        when(executor.execute(any(ToolsRequest.class))).thenReturn(Mono.just(expectedResponse));

        // When
        Mono<ToolsResponse> result = service.execute(request);

        // Then
        StepVerifier.create(result)
                .expectNext(expectedResponse)
                .verifyComplete();
    }

    @Test
    void shouldHandleToolExecutionFailure() {
        // Given
        ToolsRequest request = new ToolsRequest("invalid-tool", "test-service", null);
        RuntimeException exception = new RuntimeException("Unknown tool");

        when(factory.getExecutor(anyString())).thenThrow(exception);

        // When & Then
        StepVerifier.create(service.execute(request))
                .expectError(RuntimeException.class)
                .verify();
    }
}
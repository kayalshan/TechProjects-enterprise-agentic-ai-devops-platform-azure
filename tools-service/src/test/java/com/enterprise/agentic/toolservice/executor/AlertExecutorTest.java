package com.enterprise.agentic.toolservice.executor;

import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class AlertExecutorTest {

    private final AlertExecutor executor = new AlertExecutor();

    @Test
    void shouldReturnCorrectName() {
        assert(executor.getName().equals("send-alert"));
    }

    @Test
    void shouldExecuteAlertSuccessfully() {
        // Given
        ToolsRequest request = new ToolsRequest("send-alert", "payment-service", "Critical error detected");

        // When
        Mono<ToolsResponse> result = executor.execute(request);

        // Then
        StepVerifier.create(result)
                .expectNext(new ToolsResponse("SUCCESS", "Alert sent for: payment-service"))
                .verifyComplete();
    }
}
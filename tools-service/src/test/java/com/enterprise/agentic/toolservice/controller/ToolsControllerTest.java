package com.enterprise.agentic.toolservice.controller;

import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import com.enterprise.agentic.toolservice.service.ToolsService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ToolsControllerTest {

    private final ToolsService toolsService = mock(ToolsService.class);
    private final ToolsController controller = new ToolsController(toolsService);

    @Test
    void shouldExecuteToolSuccessfully() {
        // Given
        ToolsRequest request = new ToolsRequest("restart-service", "payment-service", null);
        ToolsResponse response = new ToolsResponse("SUCCESS", "Service restarted");
        when(toolsService.execute(any(ToolsRequest.class))).thenReturn(Mono.just(response));

        // When
        Mono<ResponseEntity<ToolsResponse>> result = controller.execute(request);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(entity -> entity.getStatusCode().is2xxSuccessful() &&
                                           entity.getBody().equals(response))
                .verifyComplete();
    }

    @Test
    void shouldReturnPingResponse() {
        // When
        Mono<String> result = controller.ping();

        // Then
        StepVerifier.create(result)
                .expectNext("Tools Service is up")
                .verifyComplete();
    }
}
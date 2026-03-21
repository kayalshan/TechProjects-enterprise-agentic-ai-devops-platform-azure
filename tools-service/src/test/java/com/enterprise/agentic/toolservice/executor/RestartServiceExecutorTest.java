package com.enterprise.agentic.toolservice.executor;

import com.enterprise.agentic.toolservice.client.KubernetesClient;
import com.enterprise.agentic.toolservice.dto.ToolsRequest;
import com.enterprise.agentic.toolservice.dto.ToolsResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestartServiceExecutorTest {

    private final KubernetesClient kubernetesClient = mock(KubernetesClient.class);
    private final RestartServiceExecutor executor = new RestartServiceExecutor(kubernetesClient);

    @Test
    void shouldReturnCorrectName() {
        assert(executor.getName().equals("restart-service"));
    }

    @Test
    void shouldExecuteRestartSuccessfully() {
        // Given
        ToolsRequest request = new ToolsRequest("restart-service", "payment-service", null);
        when(kubernetesClient.restart(anyString())).thenReturn(Mono.empty());

        // When
        Mono<ToolsResponse> result = executor.execute(request);

        // Then
        StepVerifier.create(result)
                .expectNext(new ToolsResponse("SUCCESS", "Service restarted: payment-service"))
                .verifyComplete();
    }

    @Test
    void shouldHandleRestartFailure() {
        // Given
        ToolsRequest request = new ToolsRequest("restart-service", "payment-service", null);
        RuntimeException exception = new RuntimeException("Kubernetes error");
        when(kubernetesClient.restart(anyString())).thenReturn(Mono.error(exception));

        // When
        Mono<ToolsResponse> result = executor.execute(request);

        // Then
        StepVerifier.create(result)
                .expectNext(new ToolsResponse("FAILED", "Failed to restart service: Kubernetes error"))
                .verifyComplete();
    }
}
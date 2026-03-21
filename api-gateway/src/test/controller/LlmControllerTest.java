package com.enterprise.agentic.llmservice.controller;

import com.enterprise.agentic.llmservice.dto.*;
import com.enterprise.agentic.llmservice.service.LlmService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

class LlmControllerTest {

    @Test
    void shouldReturnRcaResponse() {

        LlmService service = Mockito.mock(LlmService.class);

        RcaResponse response = new RcaResponse(
                "DB overload",
                "Latency",
                "HIGH",
                "Restart DB",
                "restart-service"
        );

        Mockito.when(service.analyze(Mockito.any()))
                .thenReturn(Mono.just(response));

        WebTestClient client = WebTestClient
                .bindToController(new LlmController(service))
                .build();

        client.post()
                .uri("/llm/analyze")
                .bodyValue(new LlmRequest("error", "context"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.rootCause").isEqualTo("DB overload");
    }
}
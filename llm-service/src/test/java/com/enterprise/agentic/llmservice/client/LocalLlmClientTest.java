package com.enterprise.agentic.llmservice.client;

import com.enterprise.agentic.llmservice.config.LlmProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalLlmClientTest {

    @Test
    void shouldConstructLocalLlmClient() {
        WebClient.Builder builder = Mockito.mock(WebClient.Builder.class);
        WebClient webClient = Mockito.mock(WebClient.class);

        Mockito.when(builder.baseUrl(Mockito.anyString())).thenReturn(builder);
        Mockito.when(builder.build()).thenReturn(webClient);

        LlmProperties props = new LlmProperties();
        props.setLocalUrl("http://localhost");

        LocalLlmClient client = new LocalLlmClient(builder, props);

        assertNotNull(client);
    }
}
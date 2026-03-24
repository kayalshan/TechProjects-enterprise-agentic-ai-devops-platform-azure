package com.enterprise.agentic.llmservice.client;

import com.enterprise.agentic.llmservice.config.LlmProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenAiClientTest {

    @Test
    void shouldConstructOpenAiClient() {
        WebClient.Builder builder = Mockito.mock(WebClient.Builder.class);
        WebClient webClient = Mockito.mock(WebClient.class);

        Mockito.when(builder.baseUrl(Mockito.anyString())).thenReturn(builder);
        Mockito.when(builder.build()).thenReturn(webClient);

        LlmProperties props = new LlmProperties();
        props.setOpenaiUrl("http://localhost");

        OpenAiClient client = new OpenAiClient(builder, props);

        assertNotNull(client);
    }
}
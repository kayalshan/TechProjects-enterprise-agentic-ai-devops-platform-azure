package com.enterprise.agentic.llmservice.util;

import com.enterprise.agentic.llmservice.dto.RcaResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    @Test
    void shouldExtractJson() {

        String input = "some text {\"rootCause\":\"DB\"} extra";

        String json = JsonUtils.extractJson(input);

        assertTrue(json.contains("rootCause"));
    }

    @Test
    void shouldParseJson() {

        String json = """
        {
          "rootCause":"DB",
          "impact":"Latency",
          "severity":"HIGH",
          "suggestedAction":"Restart",
          "toolName":"restart-service"
        }
        """;

        RcaResponse response = JsonUtils.parse(json, RcaResponse.class);

        assertEquals("DB", response.rootCause());
    }
}
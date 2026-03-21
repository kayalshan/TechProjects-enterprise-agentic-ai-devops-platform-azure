package com.enterprise.agentic.llmservice.util;

import com.enterprise.agentic.llmservice.exception.LlmException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {
        // utility class
    }

    //  Extract JSON safely from LLM response
    public static String extractJson(String text) {

        if (text == null || text.isBlank()) {
            throw new LlmException("EMPTY_RESPONSE", "LLM returned empty response");
        }

        try {
            int start = text.indexOf("{");
            int end = text.lastIndexOf("}");

            if (start == -1 || end == -1 || start > end) {
                throw new LlmException("INVALID_JSON", "No valid JSON found in response");
            }

            return text.substring(start, end + 1);

        } catch (Exception e) {
            log.error("Failed to extract JSON from response: {}", text, e);
            throw new LlmException("JSON_EXTRACTION_FAILED", "Unable to extract JSON", e);
        }
    }

    //  Parse JSON to Object
    public static <T> T parse(String json, Class<T> clazz) {

        try {
            return mapper.readValue(json, clazz);

        } catch (Exception e) {
            log.error("JSON parsing failed: {}", json, e);
            throw new LlmException("JSON_PARSE_FAILED", "Failed to parse JSON response", e);
        }
    }

    //  Convert object to JSON (optional)
    public static String toJson(Object obj) {

        try {
            return mapper.writeValueAsString(obj);

        } catch (Exception e) {
            log.error("Object to JSON conversion failed", e);
            throw new LlmException("JSON_SERIALIZATION_FAILED", "Failed to convert object to JSON", e);
        }
    }
}
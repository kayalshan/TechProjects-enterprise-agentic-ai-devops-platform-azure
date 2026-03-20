package com.enterprise.agentic.ragservice;

import com.enterprise.agentic.ragservice.dto.RagRequest;
import com.enterprise.agentic.ragservice.service.RagService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RagServiceTest {

    private final RagService ragService = new RagService();

    @Test
    void testProcessQuery() {
        var request = new RagRequest("Welcome To Agentic AI");
        var response = ragService.processQuery(request);
        assertNotNull(response);
        assertTrue(response.answer().contains("Welcome To Agentic AI"));
        assertEquals("Simulated Knowledge Base", response.source());
    }
}
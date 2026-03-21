package com.enterprise.agentic.toolservice.executor;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ToolExecutorFactory {

    private final Map<String, ToolExecutor> executors;

    public ToolExecutorFactory(Map<String, ToolExecutor> executors) {
        this.executors = executors;
    }

    public ToolExecutor getExecutor(String toolName) {
        return executors.get(toolName);
    }
}
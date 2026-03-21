package com.enterprise.agentic.toolservice.executor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ToolExecutorFactory {

    private final Map<String, ToolExecutor> executors;

    public ToolExecutorFactory(List<ToolExecutor> executorList) {
        this.executors = executorList.stream()
                .collect(Collectors.toMap(ToolExecutor::getName, e -> e));
    }

    public ToolExecutor getExecutor(String toolName) {

        ToolExecutor executor = executors.get(toolName);

        if (executor == null) {
            throw new RuntimeException("Unknown tool: " + toolName);
        }

        return executor;
    }
}
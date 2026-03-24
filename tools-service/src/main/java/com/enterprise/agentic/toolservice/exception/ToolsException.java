package com.enterprise.agentic.toolservice.exception;

public class ToolsException extends RuntimeException {

    public ToolsException(String message) {
        super(message);
    }

    public ToolsException(String message, Throwable cause) {
        super(message, cause);
    }
}
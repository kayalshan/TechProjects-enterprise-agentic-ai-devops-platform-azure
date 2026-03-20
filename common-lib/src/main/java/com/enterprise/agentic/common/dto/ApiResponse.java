package com.enterprise.agentic.common.dto;

import jakarta.validation.constraints.NotNull;

public record ApiResponse<T>(
    @NotNull T data,
    String message
) {}
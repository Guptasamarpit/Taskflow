package com.taskflow.dto;

import jakarta.validation.constraints.*;

public final class ProjectDtos {
    private ProjectDtos() {
    }

    public record ProjectRequest(@NotBlank String name, String description) {
    }

    public record ProjectResponse(Long id, String name, String description) {
    }
}
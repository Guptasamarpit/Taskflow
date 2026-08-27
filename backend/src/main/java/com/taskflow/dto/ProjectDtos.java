package com.taskflow.dto;

import com.taskflow.entity.ProjectStatus;
import jakarta.validation.constraints.*;

public final class ProjectDtos {
    private ProjectDtos() {
    }

    public record ProjectRequest(@NotBlank String name, String description, ProjectStatus status) {
    }

    public record ProjectResponse(Long id, String name, String description, ProjectStatus status) {
    }
}

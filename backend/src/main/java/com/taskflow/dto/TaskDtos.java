package com.taskflow.dto;

import com.taskflow.entity.TaskStatus;
import com.taskflow.entity.TaskPriority;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public final class TaskDtos {
    private TaskDtos() {
    }

    public record TaskRequest(@NotBlank String title, String description, TaskStatus status, TaskPriority priority, LocalDate dueDate) {
    }

    public record TaskResponse(Long id, String title, String description, TaskStatus status, TaskPriority priority, LocalDate dueDate) {
    }
}

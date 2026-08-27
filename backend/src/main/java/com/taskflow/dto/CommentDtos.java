package com.taskflow.dto;

import jakarta.validation.constraints.*;
import java.time.Instant;

public final class CommentDtos {
    private CommentDtos() {
    }

    public record CommentRequest(@NotBlank @Size(max = 2000) String content) {
    }

    public record CommentResponse(Long id, String content, Instant createdAt, Long authorId, String authorName) {
    }
}

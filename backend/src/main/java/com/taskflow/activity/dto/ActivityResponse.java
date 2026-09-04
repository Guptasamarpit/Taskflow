package com.taskflow.activity.dto;

import java.time.Instant;

public record ActivityResponse(
        Long id,
        String eventId,
        String eventType,
        Instant timestamp,
        Long userId,
        Long projectId,
        Long taskId,
        String payload
) {
}

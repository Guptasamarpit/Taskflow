package com.taskflow.event.model;

import java.time.Instant;

public record TaskFlowEvent<T>(
        String eventId,
        String eventType,
        Instant timestamp,
        Long userId,
        Long projectId,
        Long taskId,
        T payload
) {
}

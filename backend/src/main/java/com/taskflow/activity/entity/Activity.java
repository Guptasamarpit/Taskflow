package com.taskflow.activity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String eventId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private Long userId;

    private Long projectId;
    private Long taskId;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String source = "kafka";

    protected Activity() {
    }

    public Activity(String eventId, String eventType, Instant timestamp, Long userId,
                    Long projectId, Long taskId, String payload) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.userId = userId;
        this.projectId = projectId;
        this.taskId = taskId;
        this.payload = payload;
    }

    public Long getId() { return id; }
    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public Instant getTimestamp() { return timestamp; }
    public Long getUserId() { return userId; }
    public Long getProjectId() { return projectId; }
    public Long getTaskId() { return taskId; }
    public String getPayload() { return payload; }
    public String getSource() { return source; }
}

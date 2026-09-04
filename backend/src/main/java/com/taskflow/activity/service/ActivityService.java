package com.taskflow.activity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.activity.dto.ActivityResponse;
import com.taskflow.activity.entity.Activity;
import com.taskflow.activity.repository.ActivityRepository;
import com.taskflow.entity.User;
import com.taskflow.event.model.TaskFlowEvent;
import com.taskflow.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public ActivityService(ActivityRepository activityRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void persistEvent(TaskFlowEvent<?> event) {
        if (activityRepository.existsByEventId(event.eventId())) {
            return;
        }

        String payload;
        try {
            payload = objectMapper.writeValueAsString(event.payload());
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize activity payload", ex);
        }

        Activity activity = new Activity(
                event.eventId(),
                event.eventType(),
                event.timestamp(),
                event.userId(),
                event.projectId(),
                event.taskId(),
                payload
        );

        activityRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public Page<ActivityResponse> getActivitiesForUser(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return activityRepository.findByUserIdOrderByTimestampDesc(user.getId(), pageable)
                .map(activity -> new ActivityResponse(
                        activity.getId(),
                        activity.getEventId(),
                        activity.getEventType(),
                        activity.getTimestamp(),
                        activity.getUserId(),
                        activity.getProjectId(),
                        activity.getTaskId(),
                        activity.getPayload()
                ));
    }
}

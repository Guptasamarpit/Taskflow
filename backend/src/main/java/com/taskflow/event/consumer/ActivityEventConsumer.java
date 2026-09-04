package com.taskflow.event.consumer;

import com.taskflow.activity.service.ActivityService;
import com.taskflow.event.model.TaskFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class ActivityEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(ActivityEventConsumer.class);

    private final ActivityService activityService;

    public ActivityEventConsumer(ActivityService activityService) {
        this.activityService = activityService;
    }

    @KafkaListener(
            topics = "${kafka.topic.activity}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "taskFlowKafkaListenerContainerFactory"
    )
    public void onMessage(TaskFlowEvent<?> event, Acknowledgment ack) {
        try {
            activityService.persistEvent(event);
            ack.acknowledge();
        } catch (Exception ex) {
            log.error("Kafka activity processing failed for eventId={}", event.eventId(), ex);
            throw ex;
        }
    }
}

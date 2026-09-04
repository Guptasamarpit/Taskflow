package com.taskflow.event.producer;

import com.taskflow.event.model.TaskFlowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TaskFlowEventProducer {
    private static final Logger log = LoggerFactory.getLogger(TaskFlowEventProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String activityTopic;

    public TaskFlowEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${kafka.topic.activity}") String activityTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.activityTopic = activityTopic;
    }

    public void publish(String eventType, Long userId, Long projectId, Long taskId, Object payload) {
        TaskFlowEvent<Object> event = new TaskFlowEvent<>(
                java.util.UUID.randomUUID().toString(),
                eventType,
                java.time.Instant.now(),
                userId,
                projectId,
                taskId,
                payload
        );

        kafkaTemplate.send(activityTopic, String.valueOf(projectId != null ? projectId : userId), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish Kafka event {} to topic {}", eventType, activityTopic, ex);
                    } else {
                        log.info("Published Kafka event {} to topic {} at offset {}",
                                eventType, activityTopic, result.getRecordMetadata().offset());
                    }
                });
    }
}

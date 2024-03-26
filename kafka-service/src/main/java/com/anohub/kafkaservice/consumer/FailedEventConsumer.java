package com.anohub.kafkaservice.consumer;

import com.anohub.kafkaservice.event.FailedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FailedEventConsumer {

    @KafkaListener(topics = "#{'${kafka.topics.failed-event}'}")
    public void handleUserProfileDeletion(FailedEvent event) {
        log.error("{}: event failed. actions: {}, reason: {}",
                event.service(),
                event.action(),
                event.reason());
    }
}

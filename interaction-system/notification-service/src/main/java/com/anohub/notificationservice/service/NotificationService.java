package com.anohub.notificationservice.service;

import com.anohub.interactioncommon.event.NotificationEvent;
import com.anohub.notificationservice.model.Notification;
import com.anohub.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate template;

    public Mono<Notification> getNotificationByTo(String to) {
        return notificationRepository.findByToOrderByReadStatusDescSentAtAsc(to);
    }

    public Mono<Notification> markNotificationRead(String id) {
        return notificationRepository.findById(id)
                .flatMap(n -> {
                    n.setReadStatus(true);
                    return notificationRepository.save(n);
                });
    }

    @KafkaListener(topics = "#{'${kafka.topics.send-notification}'}")
    public void consumeNotificationEvent(NotificationEvent event) {

        Notification notification = Notification.builder()
                .to(event.getTo())
                .message(event.getMessage())
                .readStatus(false)
                .build();

        log.info("Received notification with message: {}", notification.getMessage());

        notificationRepository.save(notification)
                .doOnNext(
                        n -> template.convertAndSendToUser(n.getTo(), "/topic/public", n)
                ).subscribe();
    }

}



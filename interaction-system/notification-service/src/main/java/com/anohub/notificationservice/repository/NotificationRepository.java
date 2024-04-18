package com.anohub.notificationservice.repository;

import com.anohub.notificationservice.model.Notification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {
    Mono<Notification> findByToOrderByReadStatusDescSentAtAsc(String userId);
}



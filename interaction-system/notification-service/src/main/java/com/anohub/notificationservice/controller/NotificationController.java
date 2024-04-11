package com.anohub.notificationservice.controller;

import com.anohub.notificationservice.model.Notification;
import com.anohub.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("notification-app/api/v1/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/{to}")
    public Mono<ResponseEntity<Notification>> getNotificationByTo(@PathVariable String to) {
        return notificationRepository.findByTo(to)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @SendTo("/topic/public")
    @MessageMapping("/notification")
    public Notification sendNotification(Notification notification) {
        return notification;
    }
}

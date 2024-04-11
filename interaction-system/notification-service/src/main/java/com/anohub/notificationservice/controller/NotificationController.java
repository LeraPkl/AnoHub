package com.anohub.notificationservice.controller;

import com.anohub.notificationservice.model.Notification;
import com.anohub.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("notification-app/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{to}")
    public Mono<ResponseEntity<Notification>> getNotificationByTo(@PathVariable String to) {
        return notificationService.getNotificationByTo(to)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

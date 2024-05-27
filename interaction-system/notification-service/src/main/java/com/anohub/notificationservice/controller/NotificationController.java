package com.anohub.notificationservice.controller;

import com.anohub.notificationservice.model.Notification;
import com.anohub.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{to}")
    public Mono<ResponseEntity<Notification>> getNotificationByTo(@PathVariable String to) {
        return notificationService.getNotificationByTo(to)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}/mark-notification-read")
    public Mono<Notification> markNotificationRead(@PathVariable String id) {
        return notificationService.markNotificationRead(id);
    }
}

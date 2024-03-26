package com.anohub.kafkaservice.event;

public record FailedEvent(String service,
                          String action,
                          String reason) {
}

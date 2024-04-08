package com.anohub.privatechatservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatDeletionScheduler {

    private final ChatService chatService;
    private final TransactionalOperator transactionalOperator;

    @Scheduled(cron = "0 * * * * *")
    public void deleteExpiredChats() {
        LocalDateTime now = LocalDateTime
                .now(ZoneOffset.UTC);

        chatService.deleteByExpiresAt(now)
                .as(transactionalOperator::transactional);
    }
}

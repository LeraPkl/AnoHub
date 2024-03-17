package com.anohub.privatechatservice.repository;

import com.anohub.privatechatservice.model.Chat;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
    Flux<Chat> findByUser1(String senderId);
    Flux<Chat> getByExpiresAtBefore(LocalDateTime now);

    Mono<Chat> findByUser1AndUser2(String user1, String user2);
}

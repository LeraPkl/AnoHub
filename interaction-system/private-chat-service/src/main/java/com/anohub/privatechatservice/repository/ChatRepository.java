package com.anohub.privatechatservice.repository;

import com.anohub.privatechatservice.model.Chat;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
    Flux<Chat> findBySenderId(String senderId);

    Flux<Chat> findByReceiverId(String receiverId);
}
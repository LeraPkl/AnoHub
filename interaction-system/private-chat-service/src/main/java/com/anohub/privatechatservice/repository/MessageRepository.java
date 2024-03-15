package com.anohub.privatechatservice.repository;

import com.anohub.privatechatservice.model.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {
    Flux<Message> findByChatId(String chatId);
}

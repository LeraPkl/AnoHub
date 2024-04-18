package com.anohub.privatechatservice.repository;

import com.anohub.privatechatservice.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {
    Mono<Void> deleteAllByChatId(String chatId);

    Flux<Message> findAllByChatId(String chatId, Pageable pageable);
}

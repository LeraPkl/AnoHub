package com.anohub.privatechatservice.repository;

import com.anohub.privatechatservice.model.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {
    Mono<Void> deleteAllByChatId(ObjectId chatId);

    Flux<Message> findAllByChatId(ObjectId chatId, Pageable pageable);
}

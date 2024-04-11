package com.anohub.privatechatservice.repository;

import com.anohub.privatechatservice.model.Chat;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
    Flux<Chat> findByExpiresAtBeforeAndSaveChatIsFalse(LocalDateTime now);

    Mono<Chat> findByUser1_IdAndUser2_Id(Long user1Id, Long user2Id);

    @Query("{'$or': [{'user1.id': ?0}, {'user2.id': ?0}]}")
    Flux<Chat> findByUserId(String userId);
}

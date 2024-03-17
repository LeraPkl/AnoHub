package com.anohub.usermodelservice.repository;

import com.anohub.usermodelservice.model.Friends;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FriendsRepository extends ReactiveCrudRepository<Friends, Long> {
    Mono<Boolean> existsByUser1IdAndUser2Id(Long senderId, Long receiverId);
}

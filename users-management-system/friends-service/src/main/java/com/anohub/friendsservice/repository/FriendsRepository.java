package com.anohub.friendsservice.repository;

import com.anohub.friendsservice.model.Friends;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FriendsRepository extends ReactiveCrudRepository<Friends, String> {
    Mono<Boolean> existsByUser1IdAndUser2Id(String senderId, String receiverId);
}

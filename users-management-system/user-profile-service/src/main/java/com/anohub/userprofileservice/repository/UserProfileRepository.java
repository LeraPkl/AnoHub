package com.anohub.userprofileservice.repository;

import com.anohub.userprofileservice.model.UserProfile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserProfileRepository extends ReactiveCrudRepository<UserProfile, Long> {
    Mono<Void> deleteByUserId(String id);

    Mono<UserProfile> findByUserId(String id);
}

package com.anohub.userprofileservice.repository;

import com.anohub.userprofileservice.model.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface UserProfileRepository extends ReactiveCrudRepository<UserProfile, UUID> {

    Flux<UserProfile> findAllBy(Pageable pageable);
}

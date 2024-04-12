package com.anohub.feedservice.repository;

import com.anohub.feedservice.model.RepostedPost;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface RepostedPostRepository extends ReactiveMongoRepository<RepostedPost, String> {
    Flux<RepostedPost> findByUserProfileId(String userId);
}

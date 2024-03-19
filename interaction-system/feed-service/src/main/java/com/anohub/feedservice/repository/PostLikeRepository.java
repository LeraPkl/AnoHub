package com.anohub.feedservice.repository;

import com.anohub.feedservice.model.PostLike;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface PostLikeRepository extends ReactiveMongoRepository<PostLike, String> {
    Mono<Long> countByPostIdAndIsLikeTrue(String id);

    Mono<Long> countByPostIdAndIsLikeFalse(String id);

    Mono<PostLike> findByPostIdAndUserIdAndIsLikeTrue(String postId, Long userId);

    Mono<PostLike> findByPostIdAndUserIdAndIsLikeFalse(String postId, Long userId);
}

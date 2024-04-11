package com.anohub.feedservice.repository;

import com.anohub.feedservice.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Mono<Long> countByPostId(String id);

    Flux<Comment> findAllByPostId(String postId, Pageable pageable);

    Flux<Comment> findAllByCommentId(String id);
}

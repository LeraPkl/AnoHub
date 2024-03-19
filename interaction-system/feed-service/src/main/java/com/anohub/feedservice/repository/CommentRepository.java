package com.anohub.feedservice.repository;

import com.anohub.feedservice.model.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Mono<Long> countByPostId(String id);
}

package com.anohub.feedservice.repository;

import com.anohub.feedservice.model.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
}

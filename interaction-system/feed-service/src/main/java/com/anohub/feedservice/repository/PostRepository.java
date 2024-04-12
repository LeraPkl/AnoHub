package com.anohub.feedservice.repository;

import com.anohub.feedservice.model.Post;
import com.anohub.feedservice.model.RepostedPost;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
}


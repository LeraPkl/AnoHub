package com.anohub.feedservice.repository;

import com.anohub.feedservice.model.Topic;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TopicRepository extends ReactiveMongoRepository<Topic, String> {
}

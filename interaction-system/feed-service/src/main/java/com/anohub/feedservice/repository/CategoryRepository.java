package com.anohub.feedservice.repository;

import com.anohub.feedservice.model.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}

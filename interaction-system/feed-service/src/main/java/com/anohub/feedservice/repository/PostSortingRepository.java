package com.anohub.feedservice.repository;

import com.anohub.feedservice.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface PostSortingRepository extends ReactiveSortingRepository<Post, String> {
    Flux<Post> findAllBy(Pageable pageable);
}

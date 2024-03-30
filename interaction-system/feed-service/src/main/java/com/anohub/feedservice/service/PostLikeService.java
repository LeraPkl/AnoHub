package com.anohub.feedservice.service;

import com.anohub.feedservice.model.PostLike;
import com.anohub.feedservice.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final TransactionalOperator transactionalOperator;
    private final PostLikeRepository postLikeRepository;

    public Mono<ResponseEntity<Object>> toggleLike(String postId, Long userId) {
        return postLikeRepository.findByPostIdAndUserIdAndIsLikeTrue(postId, userId)
                .flatMap(postLike -> postLikeRepository.delete(postLike)
                        .then(Mono.just(ResponseEntity.noContent().build())))
                .switchIfEmpty(postLikeRepository.save(PostLike.builder()
                                .postId(postId)
                                .userId(userId)
                                .isLike(true)
                                .build())
                        .map(savedLike -> new ResponseEntity<>(savedLike, HttpStatus.CREATED)))
                .as(transactionalOperator::transactional);
    }


    public Mono<?> toggleDislike(String postId, Long userId) {
        return postLikeRepository.findByPostIdAndUserIdAndIsLikeFalse(postId, userId)
                .flatMap(postLike -> postLikeRepository.delete(postLike)
                        .then(Mono.just(ResponseEntity.noContent().build())))
                .switchIfEmpty(postLikeRepository.save(PostLike.builder()
                                .postId(postId)
                                .userId(userId)
                                .isLike(false)
                                .build())
                        .map(savedLike -> new ResponseEntity<>(savedLike, HttpStatus.CREATED)))
                .as(transactionalOperator::transactional);
    }
}

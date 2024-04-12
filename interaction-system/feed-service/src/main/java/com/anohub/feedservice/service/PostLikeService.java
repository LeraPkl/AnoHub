package com.anohub.feedservice.service;

import com.anohub.feedservice.model.PostLike;
import com.anohub.feedservice.repository.PostLikeRepository;
import com.anohub.feedservice.repository.PostRepository;
import com.anohub.interactioncommon.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {

    private final TransactionalOperator transactionalOperator;
    private final PostLikeRepository postLikeRepository;
    private final ReactiveKafkaProducerTemplate<String, NotificationEvent> producerTemplate;
    private final PostRepository postRepository;

    @Value("${kafka.topics.send-notification}")
    private String sendNotificationTopic;


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

    public Mono<?> toggleLike(String postId, Long userId) {
        return postLikeRepository.findByPostIdAndUserIdAndIsLikeTrue(postId, userId)
                .flatMap(existingLike ->
                        postLikeRepository.delete(existingLike)
                                .then(Mono.just(ResponseEntity.noContent().build()))
                )
                .switchIfEmpty(
                        Mono.defer(() -> {
                            PostLike newLike = PostLike.builder()
                                    .postId(postId)
                                    .userId(userId)
                                    .isLike(true)
                                    .build();
                            return postLikeRepository.save(newLike)
                                    .doOnNext(like -> log.info("saved post like with postId: {}", like.getId()))
                                    .flatMap(like -> sendNotificationAndReturnLike(like, postId))
                                    .map(ResponseEntity::ok);
                        })
                );
    }

    private Mono<PostLike> sendNotificationAndReturnLike(PostLike like, String postId) {
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("post with postId {} not found", postId);
                    return Mono.empty();
                }))
                .doOnNext(post -> log.info("Found post: {}", post.getTitle()))
                .flatMap(post -> producerTemplate.send(sendNotificationTopic,
                                new NotificationEvent(
                                        like.getUserId(),
                                        format("Someone liked your post \"%linkToPfp\"", post.getTitle())
                                ))
                        .doOnSuccess(s -> log.info("Notification sent for post: {}", post.getTitle()))
                        .doOnError(e -> log.error("Failed to send notification for post: {}", post.getTitle(), e)))
                .thenReturn(like)
                .doOnError(error -> log.error("error in sendNotificationAndReturnLike: {}", error.getMessage()))
                .doOnSuccess(result -> log.info("notification sent and like returned"));
    }


    public Mono<Long> countLikesByPostId(String postId) {
        return postLikeRepository.countByPostIdAndIsLikeTrue(postId);
    }

    public Mono<Long> countDislikesByPostId(String postId) {
        return postLikeRepository.countByPostIdAndIsLikeFalse(postId);
    }
}

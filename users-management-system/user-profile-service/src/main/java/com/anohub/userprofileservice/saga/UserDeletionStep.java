package com.anohub.userprofileservice.saga;

import com.anohub.kafkaservice.event.SagaStep;
import com.anohub.usermodelservice.event.DeleteUserEvent;
import com.anohub.usermodelservice.event.DeleteUserEventRollbackEvent;
import com.anohub.usermodelservice.event.DeletedUserProfileEvent;
import com.anohub.userprofileservice.model.UserProfile;
import com.anohub.userprofileservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserDeletionStep implements SagaStep<DeleteUserEvent, DeleteUserEventRollbackEvent> {

    public static final String USER_PROFILE = "user_profile";
    private final UserProfileService userService;
    private final ReactiveRedisTemplate<String, UserProfile> redisTemplate;
    private final KafkaTemplate<String, DeletedUserProfileEvent> kafkaTemplate;

    @Value("${kafka.topics.user-profile-deleted}")
    private String userProfileDeletedTopic;

    @Override
    @KafkaListener(topics = "#{'${kafka.topics.delete-user-profile-request}'}")
    public Mono<Void> process(DeleteUserEvent event) {
        UUID userId = event.getUserId();
        return userService.getUserProfileById(userId)
                .flatMap(u ->
                        redisTemplate.opsForValue()
                                .set(USER_PROFILE, u)
                                .then(Mono.defer(() -> {
                                    log.info("Deleting user with id {}...", userId);
                                    return userService.deleteById(userId);
                                }))
                                .then(Mono.defer(() -> {
                                            log.info("Sending DeletedUserProfile Event with userId {}...", userId);
                                            kafkaTemplate.send(userProfileDeletedTopic,
                                                    new DeletedUserProfileEvent(userId));
                                            return Mono.empty();
                                        })
                                )
                );
    }

    @Override
    @KafkaListener(topics = "#{'${kafka.topics.user-profile-deleted-rollback}'}")
    public Mono<Void> rollback(DeleteUserEventRollbackEvent event) {
        return redisTemplate.opsForValue().get(USER_PROFILE)
                .flatMap(cachedProfile -> {
                    if (Objects.isNull(cachedProfile)) {
                        return Mono.error(new RuntimeException("Cached profile not found"));
                    }
                    return userService.createUserProfile(cachedProfile)
                            .doOnSuccess((f) -> redisTemplate.delete(USER_PROFILE))
                            .then();
                });
    }
}

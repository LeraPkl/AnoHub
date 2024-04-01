package com.anohub.userprofileservice.saga;

import com.anohub.kafkaservice.event.SagaStep;
import com.anohub.usermodelservice.event.UserProfileCreatedRollbackEvent;
import com.anohub.userprofileservice.model.CreateUserProfileEvent;
import com.anohub.userprofileservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserCreationStep implements SagaStep<CreateUserProfileEvent, UserProfileCreatedRollbackEvent> {

    private final UserProfileService userProfileService;

    @Override
//    @KafkaListener(topics = "${kafka.topics.create-user-profile-request}")
    public Mono<Void> process(CreateUserProfileEvent event) {
        return userProfileService
                .createUserProfile(event.getUserProfile())
                .then();
    }

    @Override
    @KafkaListener(topics = "${kafka.topics.user-profile-created-rollback}")
    public Mono<Void> rollback(UserProfileCreatedRollbackEvent event) {
        return userProfileService.deleteById(event.getUserId());
    }
}

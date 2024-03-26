package com.anohub.userprofileservice.service;

import com.anohub.kafkaservice.event.FailedEvent;
import com.anohub.usermodelservice.event.DeleteUserProfileEvent;
import com.anohub.userprofileservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    @Value("${kafka.topics.failed-event}")
    private String failedEventTopic;

    private final KafkaTemplate<String, FailedEvent> kafkaTemplate;

    private final UserProfileRepository userProfileRepository;

    @KafkaListener(topics = "#{'${kafka.topics.user-profile-deleted}'}")
    public void handleUserProfileDeletion(DeleteUserProfileEvent event) {

        userProfileRepository.findByUserId(event.getUserId())
                .doOnNext(userProfile -> log.info("deleting user profile with user id: {}", userProfile.getUserId()))
                .flatMap(userProfileRepository::delete)
                .doOnError(e ->
                        kafkaTemplate.send(failedEventTopic,
                                new FailedEvent("user-profile-service", "DELETE_USER", "test"))
                )
                .subscribe();
    }

}

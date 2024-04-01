package com.anohub.userprofileservice.service;

import com.anohub.userprofileservice.model.UserProfile;
import com.anohub.userprofileservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final KafkaSender<String, UserProfile> kafkaSender;

    @Value("${kafka.topics.user-profile-created}")
    private String userProfileCreatedTopic;


    public Mono<UserProfile> createUserProfile(UserProfile userProfile) {
        UUID id = UUID.randomUUID();
        userProfile.setId(id);
        return userProfileRepository.save(userProfile)
                .flatMap(savedUserProfile -> {
                    if (savedUserProfile.getId() != null) {
                        return kafkaSender.send(
                                        Mono.just(SenderRecord
                                                .create(new ProducerRecord<>(
                                                                userProfileCreatedTopic,
                                                                savedUserProfile),
                                                        null)))
                                .then(Mono.just(savedUserProfile));
                    } else {
                        return Mono.error(new RuntimeException("Failed to create user profile"));
                    }
                });
    }

    public Mono<UserProfile> updateUserProfile(UUID id, UserProfile userProfile) {
        return userProfileRepository.findById(id)
                .flatMap(existingProfile -> {
                    BeanUtils.copyProperties(userProfile, existingProfile, "id");
                    return userProfileRepository.save(existingProfile);
                });
    }

    public Mono<UserProfile> getUserProfileById(UUID id) {
        return userProfileRepository.findById(id);
    }

    public Flux<UserProfile> getAllUserProfiles(Pageable pageable) {
        return userProfileRepository.findAllBy(pageable);
    }

    public Mono<Void> deleteById(UUID userId) {
        return userProfileRepository.deleteById(userId);
    }
}


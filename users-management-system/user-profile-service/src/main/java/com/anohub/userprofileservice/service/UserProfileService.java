package com.anohub.userprofileservice.service;

import com.anohub.userprofileservice.model.UserProfile;
import com.anohub.userprofileservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public Mono<UserProfile> createUserProfile(UUID id, UserProfile userProfile) {
        userProfile.setId(id);
        return userProfileRepository.save(userProfile);
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


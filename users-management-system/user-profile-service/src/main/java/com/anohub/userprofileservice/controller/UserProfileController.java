package com.anohub.userprofileservice.controller;

import com.anohub.userprofileservice.model.UserProfile;
import com.anohub.userprofileservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-profiles-app/api/v1/user-profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserProfile> create(UUID id, @RequestBody UserProfile userProfile) {
        return userProfileService.createUserProfile(id, userProfile);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserProfile>> update(@PathVariable UUID id,
                                                    @RequestBody @Valid UserProfile userProfile) {
        return userProfileService.updateUserProfile(id, userProfile)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserProfile>> getByUserId(@PathVariable UUID id) {
        return userProfileService.getUserProfileById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<UserProfile> getAll(
            @PageableDefault(size = 10) Pageable pageable) {
        return userProfileService.getAllUserProfiles(pageable);
    }
}

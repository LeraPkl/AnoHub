package com.anohub.usermodelservice.controller;

import com.anohub.usermodelservice.model.Friends;
import com.anohub.usermodelservice.service.FriendsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user-model-app/api/v1/friends")
public class FriendsController {

    private final FriendsService friendsService;

    @PostMapping("/send-request/{receiverId}")
    public Mono<Friends> sendFriendRequest(@PathVariable Long receiverId) {

        // TODO: Add authentication
        Long senderId = 1L;
        return friendsService.sendFriendRequest(senderId, receiverId);
    }

}

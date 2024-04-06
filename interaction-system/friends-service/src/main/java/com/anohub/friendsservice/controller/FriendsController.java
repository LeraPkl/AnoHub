package com.anohub.friendsservice.controller;

import com.anohub.friendsservice.model.Friends;
import com.anohub.friendsservice.service.FriendsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user-model-app/api/v1/friends")
public class FriendsController {

    private final FriendsService friendsService;

    @PostMapping("/send-request/{senderId}/{receiverId}")
    public Mono<Friends> sendFriendRequest(@PathVariable String receiverId,
                                           @PathVariable String senderId) {

        return friendsService.sendFriendRequest(senderId, receiverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/remove-friend/{senderId}/{receiverId}")
    public Mono<Void> removeFriend(@PathVariable String receiverId,
                                   @PathVariable String senderId) {

        return friendsService.removeFriend(senderId, receiverId);
    }

}

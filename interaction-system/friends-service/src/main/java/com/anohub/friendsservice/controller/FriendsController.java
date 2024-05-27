package com.anohub.friendsservice.controller;

import com.anohub.friendsservice.model.FriendRequestMessage;
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
@RequestMapping("/api/v1/friends")
public class FriendsController {

    private final FriendsService friendsService;

    @PostMapping("/send-request/{senderId}/{receiverId}")
    public Mono<Friends> sendFriendRequest(@PathVariable String receiverId,
                                           @PathVariable String senderId,
                                           @RequestBody(required = false) FriendRequestMessage friendRequestMessage) {
        String message = (friendRequestMessage != null)
                ? friendRequestMessage.message()
                : null;
        return friendsService.sendFriendRequest(senderId, receiverId, message);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/remove-friend/{senderId}/{receiverId}")
    public Mono<Void> removeFriend(@PathVariable String receiverId,
                                   @PathVariable String senderId) {
        return friendsService.removeFriend(senderId, receiverId);
    }

    @PostMapping("/accept")
    public Mono<Friends> acceptFriendRequest(@RequestParam String senderId,
                                             @RequestParam String receiverId) {
        return friendsService.acceptFriendRequest(senderId, receiverId);
    }

    @PostMapping("/decline")
    public Mono<Void> declineFriendRequest(@RequestParam String senderId,
                                           @RequestParam String receiverId) {
        return friendsService.declineFriendRequestAcceptance(senderId, receiverId);
    }
}

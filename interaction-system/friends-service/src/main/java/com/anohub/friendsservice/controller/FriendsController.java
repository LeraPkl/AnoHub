package com.anohub.friendsservice.controller;

import com.anohub.friendsservice.model.Friends;
import com.anohub.friendsservice.service.FriendsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
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
    public Mono<Friends> sendFriendRequest(@PathVariable String receiverId,
                                           @AuthenticationPrincipal DefaultOidcUser user) {

        return friendsService.sendFriendRequest(user.getPreferredUsername(), receiverId);
    }

}

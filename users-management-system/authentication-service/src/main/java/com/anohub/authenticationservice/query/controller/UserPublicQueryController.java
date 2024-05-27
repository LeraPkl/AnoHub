package com.anohub.authenticationservice.query.controller;

import com.anohub.authenticationservice.model.User;
import com.anohub.authenticationservice.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/authentication-app/api/v1/public/users")
@RequiredArgsConstructor
public class UserPublicQueryController {

    private final UserQueryService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> getUserById(@PathVariable String id) {
        return Mono.just(userService.getUserById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<User>> getUsers() {
        return Mono.just(userService.getUsers());
    }
}

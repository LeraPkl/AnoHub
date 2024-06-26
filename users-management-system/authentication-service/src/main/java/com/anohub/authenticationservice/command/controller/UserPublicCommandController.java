package com.anohub.authenticationservice.command.controller;

import com.anohub.authenticationservice.command.controller.model.CreateUserCommand;
import com.anohub.authenticationservice.command.service.UserCommandService;
import com.anohub.authenticationservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/public/users")
@RequiredArgsConstructor
public class UserPublicCommandController {

    private final UserCommandService userService;

    @PostMapping("/create-user")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createPendingUser(@RequestBody CreateUserCommand request) {
        return userService.createUser(request);
    }

}

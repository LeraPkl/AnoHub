package com.anohub.authenticationservice.controller;

import com.anohub.authenticationservice.model.User;
import com.anohub.authenticationservice.model.dto.CreateUserRequest;
import com.anohub.authenticationservice.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/authentication-app/api/v1/public/users")
@RequiredArgsConstructor
public class UserPublicController {

    private final KeycloakService keycloakService;


    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {
        return Mono.just(ResponseEntity.ok(keycloakService.getUserById(id)));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> signup(@RequestBody CreateUserRequest request) {
        return Mono.just(keycloakService.createUser(request));
    }

    @GetMapping
    public Mono<ResponseEntity<List<User>>> getUsers() {
        return Mono.just(ResponseEntity.ok(keycloakService.getUsers()));
    }
}

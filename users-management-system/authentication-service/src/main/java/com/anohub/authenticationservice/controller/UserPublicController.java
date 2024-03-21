package com.anohub.authenticationservice.controller;

import com.anohub.authenticationservice.model.KeycloakService;
import com.anohub.authenticationservice.model.User;
import com.anohub.authenticationservice.model.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
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
    public Mono<ResponseEntity<User>> signup(@RequestBody CreateUserRequest request) {
        return Mono.just(ResponseEntity.ok(keycloakService.createUser(request)));
    }

    @GetMapping
    public Mono<ResponseEntity<List<User>>> getUsers() {
        return Mono.just(ResponseEntity.ok(keycloakService.getUsers()));
    }
}

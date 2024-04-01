package com.anohub.authenticationservice.util;


import com.anohub.authenticationservice.exception.EmailAlreadyExistsException;
import com.anohub.authenticationservice.exception.UnexpectedResponseStatusException;
import com.anohub.authenticationservice.exception.UserRepresentationNotFoundException;
import com.anohub.authenticationservice.model.User;
import com.anohub.authenticationservice.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
@Component
public class UserCreator {

    private final KeycloakService keycloakService;

    public Mono<User> createUserFromCachedRepresentation(Mono<UserRepresentation> userRepresentation) {
        return userRepresentation
                .switchIfEmpty(Mono.error(new UserRepresentationNotFoundException("Pending user not found")))
                .flatMap(this::createUserInKeycloak)
                .onErrorMap(Exception.class, this::handleUserCreationException)
                .doOnError(e -> log.error("Error during user creation: {}", e.getMessage()));
    }

    private Mono<User> createUserInKeycloak(UserRepresentation userRepresentation) {
        Response response = keycloakService.getUsersResource().create(userRepresentation);
        return handleKeycloakResponse(response, userRepresentation);
    }

    private Mono<User> handleKeycloakResponse(Response response, UserRepresentation userRepresentation) {
        switch (response.getStatus()) {
            case 201:
                String userId = CreatedResponseUtil.getCreatedId(response);
                keycloakService.addRole(userId);
                return Mono.just(new User(userId, userRepresentation.getEmail()));
            case 409:
                throw new EmailAlreadyExistsException(userRepresentation.getEmail());
            default:
                throw new UnexpectedResponseStatusException(response.getStatus());
        }
    }

    private Throwable handleUserCreationException(Exception e) {
        log.error("User creation failed due to: {}", e.getMessage());
        return e;
    }
}

package com.anohub.authenticationservice.command.service;

import com.anohub.authenticationservice.command.controller.model.CreateUserCommand;
import com.anohub.authenticationservice.model.User;
import com.anohub.authenticationservice.service.KeycloakService;
import com.anohub.authenticationservice.util.UserCreator;
import com.anohub.usermodelservice.event.deletion.DeleteUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.anohub.authenticationservice.service.KeycloakService.createUserRepresentation;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCommandService {

    private final UserCreator userCreator;
    private final KeycloakService keycloakService;
    private final ReactiveKafkaProducerTemplate<String, DeleteUserEvent> kafkaProducerTemplate;

    @Value("${kafka.topics.delete-user-profile-request}")
    private String deleteUserProfileRequest;

    @Retryable
    public Mono<User> createUser(CreateUserCommand command) {

        UserRepresentation user = createUserRepresentation(command);
        String id = command.id().toString();
        user.setId(id);

        return userCreator.createUserFromRepresentation(Mono.just(user));
    }

    public Mono<Void> requestUserDeletionById(UUID userId) {
        return kafkaProducerTemplate.send(
                        deleteUserProfileRequest,
                        new DeleteUserEvent(userId))
                .then();
    }

    public void verifyEmail(String userId) {

        UsersResource usersResource = keycloakService.getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    public void updatePassword(String userId) {

        UserResource userResource = keycloakService.getUsersResource().get(userId);
        List<String> actions = new ArrayList<>();
        actions.add("UPDATE_PASSWORD");
        userResource.executeActionsEmail(actions);

    }
}

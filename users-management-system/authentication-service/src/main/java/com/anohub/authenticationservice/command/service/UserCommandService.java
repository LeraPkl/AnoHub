package com.anohub.authenticationservice.command.service;

import com.anohub.authenticationservice.command.controller.model.CreateUserCommand;
import com.anohub.authenticationservice.exception.EmailAlreadyExistsException;
import com.anohub.authenticationservice.model.User;
import com.anohub.authenticationservice.service.KeycloakService;
import com.anohub.kafkaservice.producer.KafkaProducer;
import com.anohub.usermodelservice.event.DeleteUserProfileEvent;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.anohub.authenticationservice.service.KeycloakService.createUserRepresentation;
import static com.anohub.authenticationservice.service.KeycloakService.setCredentials;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final KeycloakService keycloakService;
    private final KafkaProducer<DeleteUserProfileEvent> producer;


    @Value("${kafka.topics.user-profile-deleted}")
    private String userProfileDeleteTopic;

    @Transactional
    public User createUser(CreateUserCommand request) throws EmailAlreadyExistsException {

        UserRepresentation user = createUserRepresentation(request);
        setCredentials(user, request.password());
        UsersResource usersResource = keycloakService.getUsersResource();

        try (Response response = usersResource.create(user)) {

            if (Objects.equals(201, response.getStatus())) {

                var userId = CreatedResponseUtil.getCreatedId(response);
                keycloakService.addRole(userId);

                return User.builder()
                        .id(userId)
                        .email(request.email())
                        .build();
            } else if (Objects.equals(409, response.getStatus())) {
                throw new EmailAlreadyExistsException(request.email());
            }
        }
        return null;
    }


    @Transactional
    public void deleteUserById(UUID userId) {
        try (Response response = keycloakService.getUsersResource().delete(userId.toString())) {
            producer.produce(userProfileDeleteTopic, new DeleteUserProfileEvent(userId));
        }
    }

    public void emailVerification(String userId) {

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

package com.anohub.authenticationservice.service;


import com.anohub.authenticationservice.exception.EmailAlreadyExistsException;
import com.anohub.authenticationservice.model.User;
import com.anohub.authenticationservice.model.dto.CreateUserRequest;
import com.anohub.kafkaservice.producer.KafkaProducer;
import com.anohub.usermodelservice.event.DeleteUserProfileEvent;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String ROLE_USER = "ROLE_USER";

    private final Keycloak keycloakClient;

    private final KafkaProducer<DeleteUserProfileEvent> producer;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${kafka.topics.user-profile-deleted}")
    private String userProfileDeleteTopic;

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        RealmResource realmResource = keycloakClient.realm(realm);

        return realmResource.users()
                .list()
                .stream()
                .map(KeycloakService::getUserFromRepresentation)
                .toList();
    }

    @Transactional(readOnly = true)
    public User getUserById(String id) {
        RealmResource realmResource = keycloakClient.realm(realm);

        UserRepresentation userRepresentation = realmResource.users()
                .get(id)
                .toRepresentation();

        return getUserFromRepresentation(userRepresentation);
    }

    @Transactional
    public User createUser(CreateUserRequest request) throws EmailAlreadyExistsException {

        UserRepresentation user = createUserRepresentation(request);
        setCredentials(user, request.password());
        UsersResource usersResource = getUsersResource();

        try (Response response = usersResource.create(user)) {

            if (Objects.equals(201, response.getStatus())) {

                var userId = CreatedResponseUtil.getCreatedId(response);
                addRole(userId);

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

    private void addRole(String userId) {
        UserResource userResource = getUsersResource().get(userId);
        var role = keycloakClient.realm(realm)
                .roles()
                .get(ROLE_USER)
                .toRepresentation();

        userResource.roles()
                .realmLevel()
                .add(List.of(role));
    }

    private void setCredentials(UserRepresentation user, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        user.setCredentials(List.of(credential));
        user.setRealmRoles(List.of(ROLE_USER));
    }

    @Transactional
    public Mono<Void> deleteUserById(String userId) {
        return Mono.fromRunnable(() -> {
            try (Response response = getUsersResource().delete(userId)) {
                producer.produce(userProfileDeleteTopic, new DeleteUserProfileEvent(userId));
            }
        });
    }

    public void emailVerification(String userId) {

        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    public void updatePassword(String userId) {

        UserResource userResource = getUsersResource().get(userId);
        List<String> actions = new ArrayList<>();
        actions.add("UPDATE_PASSWORD");
        userResource.executeActionsEmail(actions);

    }

    private UserRepresentation createUserRepresentation(CreateUserRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setEmail(request.email());
        user.setEmailVerified(false);
        user.setEnabled(true);

        return user;
    }

    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloakClient.realm(realm);
        return realm1.users();
    }

    private static User getUserFromRepresentation(UserRepresentation ur) {
        return User.builder()
                .id(ur.getId())
                .email(ur.getEmail())
                .build();
    }

}






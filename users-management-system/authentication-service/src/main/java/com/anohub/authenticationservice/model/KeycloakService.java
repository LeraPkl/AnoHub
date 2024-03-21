package com.anohub.authenticationservice.model;


import com.anohub.authenticationservice.model.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String ROLE_USER = "ROLE_USER";

    private final Keycloak keycloakClient;

    @Value("${keycloak.realm}")
    private String realm;

    public List<User> getUsers() {
        RealmResource realmResource = keycloakClient.realm(realm);

        return realmResource.users()
                .list()
                .stream()
                .map(it -> User.builder()
                        .id(it.getId())
                        .username(it.getUsername())
                        .email(it.getEmail())
                        .build())
                .toList();
    }

    public User getUserById(String id) {
        RealmResource realmResource = keycloakClient.realm(realm);

        UserRepresentation userRepresentation = realmResource.users()
                .get(id)
                .toRepresentation();

        return User.builder()
                .id(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .build();
    }

    public User createUser(CreateUserRequest request) {
        RealmResource realmResource = keycloakClient.realm(realm);

        UserRepresentation user = createUserRepresentation(request);

        setCredentials(user, request.password());

        var response = realmResource.users().create(user);
        var userId = CreatedResponseUtil.getCreatedId(response);

        addRole(realmResource, userId);

        return User.builder()
                .id(userId)
                .username(request.username())
                .email(request.email())
                .build();
    }

    private void addRole(RealmResource realmResource, String userId) {
        UserResource userResource = realmResource.users().get(userId);
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

    private UserRepresentation createUserRepresentation(CreateUserRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setEmailVerified(true);
        user.setEnabled(true);

        return user;
    }
}

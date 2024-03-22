package com.anohub.authenticationservice.model;


import com.anohub.authenticationservice.model.dto.CreateUserRequest;
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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord) {

        UserRepresentation user = getUserRepresentation(userRegistrationRecord);

        UsersResource usersResource = getUsersResource();

        try (Response response = usersResource.create(user)) {

            if (Objects.equals(201, response.getStatus())) {

                List<UserRepresentation> representationList = usersResource.searchByUsername(userRegistrationRecord.username(), true);
                if (!CollectionUtils.isEmpty(representationList)) {
                    UserRepresentation userRepresentation1 = representationList.stream().filter(userRepresentation -> Objects.equals(false, userRepresentation.isEmailVerified())).findFirst().orElse(null);
                    assert userRepresentation1 != null;
                    emailVerification(userRepresentation1.getId());
                }
                return userRegistrationRecord;
            }
        }

        return null;
    }


    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloakClient.realm(realm);
        return realm1.users();
    }


    public void deleteUserById(String userId) {
        getUsersResource().delete(userId);
    }


    public void emailVerification(String userId) {

        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    public UserResource getUserResource(String userId) {
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    public void updatePassword(String userId) {

        UserResource userResource = getUserResource(userId);
        List<String> actions = new ArrayList<>();
        actions.add("UPDATE_PASSWORD");
        userResource.executeActionsEmail(actions);

    }

    private static UserRepresentation getUserRepresentation(UserRegistrationRecord userRegistrationRecord) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRegistrationRecord.username());
        user.setEmail(userRegistrationRecord.email());
        user.setFirstName(userRegistrationRecord.firstName());
        user.setLastName(userRegistrationRecord.lastName());
        user.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userRegistrationRecord.password());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        List<CredentialRepresentation> list = new ArrayList<>();
        list.add(credentialRepresentation);
        user.setCredentials(list);
        return user;
    }

    public record UserRegistrationRecord(String username,
                                         String email,
                                         String firstName,
                                         String lastName,
                                         String password) {
    }

}



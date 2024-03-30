package com.anohub.authenticationservice.command.service;

import com.anohub.authenticationservice.command.controller.model.CreateUserCommand;
import com.anohub.authenticationservice.exception.EmailAlreadyExistsException;
import com.anohub.authenticationservice.model.User;
import com.anohub.authenticationservice.service.KeycloakService;
import com.anohub.usermodelservice.event.DeleteUserEvent;
import com.anohub.usermodelservice.event.DeletedUserProfileEvent;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.anohub.authenticationservice.service.KeycloakService.createUserRepresentation;
import static com.anohub.authenticationservice.service.KeycloakService.setCredentials;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCommandService {

    private final KeycloakService keycloakService;
    private final KafkaTemplate<String, DeleteUserEvent> kafkaTemplate;

    @Value("${kafka.topics.delete-user-profile-request}")
    private String deleteUserProfileRequest;

    @Value("${kafka.topics.user-profile-deleted-rollback}")
    private String userProfileDeletedRollbackTopic;

    @Transactional
    public Mono<User> createUser(CreateUserCommand request) throws EmailAlreadyExistsException {

        UserRepresentation user = createUserRepresentation(request);
        setCredentials(user, request.password());
        UsersResource usersResource = keycloakService.getUsersResource();

        try (Response response = usersResource.create(user)) {

            if (Objects.equals(201, response.getStatus())) {

                var userId = CreatedResponseUtil.getCreatedId(response);
                keycloakService.addRole(userId);

                return Mono.create(c -> c.success(
                        User.builder()
                                .id(userId)
                                .email(request.email())
                                .build()));
            } else if (Objects.equals(409, response.getStatus())) {
                throw new EmailAlreadyExistsException(request.email());
            }
        } catch (Exception e) {
            log.error("User creation failed for email: {} due to: {}", request.email(), e.getMessage());
            return Mono.error(e);
        }
        return Mono.empty();
    }

    public void requestUserDeletionById(UUID userId) {
        kafkaTemplate.send(deleteUserProfileRequest, new DeleteUserEvent(userId));
    }

    // TODO: Mono instead of string
    @KafkaListener(topics = "#{'${kafka.topics.user-profile-deleted}'}")
    public void deleteUserById(DeletedUserProfileEvent event) {

        String userId = event.getUserId().toString();
        try (Response delete = keycloakService.getUsersResource().delete(userId)) {

            log.info("User deletion requested for userId: {}. status is {}", userId, delete.getStatus());

            if (delete.getStatus() == 204) {
                log.info("User deletion successful for userId: {}", userId);
//                return Mono.just("User deletion successful for userId: " + userId);
            } else {
                throw new RuntimeException("Deletion failed. Response Status is " + delete.getStatus());
            }
        } catch (Exception e) {
            kafkaTemplate.send(userProfileDeletedRollbackTopic, null);
            log.info("User deletion failed for userId: {} due to: {}", event, e.getMessage());
//            return Mono.just(
//                    format("User deletion failed for userId: %s due to: %s",
//                            userId,
//                            e.getMessage())
        }
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

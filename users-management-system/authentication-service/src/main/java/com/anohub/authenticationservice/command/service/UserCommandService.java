package com.anohub.authenticationservice.command.service;

import com.anohub.authenticationservice.command.controller.model.CreateUserCommand;
import com.anohub.authenticationservice.model.User;
import com.anohub.authenticationservice.service.KeycloakService;
import com.anohub.authenticationservice.util.Constants;
import com.anohub.authenticationservice.util.UserCreator;
import com.anohub.usermodelservice.event.DeleteUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.anohub.authenticationservice.service.KeycloakService.createUserRepresentation;
import static com.anohub.authenticationservice.util.Constants.PENDING_USER_REPRESENTATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCommandService {

    private final UserCreator userCreator;
    private final KeycloakService keycloakService;
    private final KafkaSender<String, DeleteUserEvent> kafkaSender;
    private final ReactiveRedisTemplate<String, UserRepresentation> redisTemplate;

    @Value("${kafka.topics.delete-user-profile-request}")
    private String deleteUserProfileRequest;

    public Mono<Void> createPendingUser(CreateUserCommand createUserCommand) {

        UserRepresentation user = createUserRepresentation(createUserCommand);

        UUID id = UUID.randomUUID();
        user.setId(id.toString());

        return redisTemplate.opsForValue()
                .set(PENDING_USER_REPRESENTATION, user)
                .then();
    }

    public Mono<User> createUser() {
        return userCreator.createUserFromCachedRepresentation(
                redisTemplate.hasKey(Constants.PENDING_USER_REPRESENTATION).flatMap(hasKey -> {
                    if (hasKey) {
                        return redisTemplate.opsForValue()
                                .get(Constants.PENDING_USER_REPRESENTATION);
                    }
                    return Mono.empty();
                })
        );
//                redisTemplate.opsForValue()
//                        .get(Constants.PENDING_USER_REPRESENTATION).log());
    }

    public Mono<Void> requestUserDeletionById(UUID userId) {
        return kafkaSender.send(
                        Mono.just(SenderRecord.create(
                                new ProducerRecord<>(deleteUserProfileRequest,
                                        new DeleteUserEvent(userId)),
                                null
                        )))
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

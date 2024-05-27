package com.anohub.feedservice.client;

import com.anohub.feedservice.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserClient {

    private final WebClient webClient;
    private final String USER_MODEL_API_URL = "/user-profile-service/api/v1/user-profiles/";

    @Retryable
    public Mono<UserDto> getUser(String id) {
        return webClient.get()
                .uri(USER_MODEL_API_URL + id)
                .retrieve()
                .bodyToMono(UserDto.class);
    }
}

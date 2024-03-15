package com.anohub.feedservice.client;

import com.anohub.feedservice.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final WebClient webClient;
    private final String USER_MODEL_API_URL = "/user-model-app/api/v1/users/";

    public Mono<UserDto> getUser(Long id) {
        return webClient.get()
                .uri(USER_MODEL_API_URL + id)
                .retrieve()
                .bodyToMono(UserDto.class);
    }
}

package com.anohub.authenticationservice.controller;

import com.anohub.authenticationservice.service.KeycloakService;
import com.anohub.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/authentication-app/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final KeycloakService keycloakService;
    private final UserService userService;

    @GetMapping("/token")
    public Mono<String> getToken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                 @AuthenticationPrincipal DefaultOidcUser user) {
        log.info(authorizedClient.getPrincipalName());
        log.info(user.getPreferredUsername());
        return Mono.just(authorizedClient.getAccessToken().getTokenValue());
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteAuthenticatedUser(
            @AuthenticationPrincipal Jwt jwt) {
        String id = userService.getPrincipalFromJwt(jwt).getId();
        return keycloakService.deleteUserById(id);
    }
}



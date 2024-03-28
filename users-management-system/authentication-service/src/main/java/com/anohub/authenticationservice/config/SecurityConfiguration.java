package com.anohub.authenticationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtAuthenticationConverter authenticationConverter) {
        return http
//                .cors(ServerHttpSecurity.CorsSpec::disable)
//                .csrf(it -> it.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()))
                .cors(withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(it -> it.pathMatchers("/authentication-app/api/*/public/**", "/").permitAll()
                        .anyExchange()
                        .authenticated()
                )
                .oauth2Login(withDefaults())
                .oauth2ResourceServer(it -> it.jwt(withDefaults()))
                //   .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }

}



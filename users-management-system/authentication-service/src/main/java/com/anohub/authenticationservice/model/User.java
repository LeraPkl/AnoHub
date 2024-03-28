package com.anohub.authenticationservice.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String id;

    private String email;

    private List<? extends GrantedAuthority> authorities;

}

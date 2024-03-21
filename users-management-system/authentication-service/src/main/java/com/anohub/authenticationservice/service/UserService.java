package com.anohub.authenticationservice.service;

import com.anohub.authenticationservice.model.User;
import com.anohub.authenticationservice.util.JwtUtil;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User getPrincipalFromJwt(Jwt jwt) {
        return JwtUtil.getUserFromToken(jwt);
    }
}

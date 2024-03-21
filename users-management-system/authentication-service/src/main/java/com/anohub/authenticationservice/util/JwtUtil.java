package com.anohub.authenticationservice.util;

import com.anohub.authenticationservice.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUtil {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String PREFERRED_USERNAME = "preferred_username";
    private static final String EMAIL = "email";

    public static User getUserFromToken(Jwt jwt) {
        return User.builder()
                .id(getSubject(jwt))
                .username(jwt.getClaim(PREFERRED_USERNAME))
                .email(jwt.getClaim(EMAIL))
                .build();
    }

    public static String getSubject(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        return jwt.getClaim(claimName);
    }

    public static Set<SimpleGrantedAuthority> extractResourceRoles(Jwt jwt) {

        Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);

        if (realmAccess == null) {
            return Set.of();
        }

        Collection<String> resourceRoles = (Collection<String>) realmAccess.get(ROLES);

        return resourceRoles
                .stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}

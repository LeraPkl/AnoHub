package com.anohub.authenticationservice.model.dto;

import java.util.List;

public record AuthenticationResponse(String id,
                                     String accessToken,
                                     String refreshToken,
                                     Long expiresAt,
                                     List<String> authorities) {
}

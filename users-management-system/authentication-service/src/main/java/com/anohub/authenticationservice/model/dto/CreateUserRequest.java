package com.anohub.authenticationservice.model.dto;

public record CreateUserRequest(String username,
                                String password,
                                String email) {
}

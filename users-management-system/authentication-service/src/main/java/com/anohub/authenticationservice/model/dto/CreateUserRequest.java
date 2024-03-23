package com.anohub.authenticationservice.model.dto;

public record CreateUserRequest(String password,
                                String email) {
}

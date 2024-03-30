package com.anohub.authenticationservice.command.controller.model;

public record CreateUserCommand(String password,
                                String email) {
}

package com.anohub.authenticationservice.command.controller.model;

import java.util.UUID;

public record CreateUserCommand(UUID id,
                                String password,
                                String email) {
}

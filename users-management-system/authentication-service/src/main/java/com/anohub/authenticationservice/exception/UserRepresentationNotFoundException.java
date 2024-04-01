package com.anohub.authenticationservice.exception;

public class UserRepresentationNotFoundException extends RuntimeException {
    public UserRepresentationNotFoundException(String pendingUserNotFound) {
        super(pendingUserNotFound);
    }
}

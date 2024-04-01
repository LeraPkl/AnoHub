package com.anohub.authenticationservice.exception;

public class UnexpectedResponseStatusException extends RuntimeException {
    public UnexpectedResponseStatusException(int status) {
        super("Unexpected response status: " + status);
    }
}

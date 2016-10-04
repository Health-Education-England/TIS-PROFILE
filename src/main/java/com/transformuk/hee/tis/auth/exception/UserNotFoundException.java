package com.transformuk.hee.tis.auth.exception;

/**
 * Thrown when no user with given user name found
 */
public class UserNotFoundException extends Exception {
    private String message;

    public UserNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

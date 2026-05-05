package io.github.SzymonWoroniecki.gym_membership_system.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Member with email '" + email + "' already exists");
    }
}
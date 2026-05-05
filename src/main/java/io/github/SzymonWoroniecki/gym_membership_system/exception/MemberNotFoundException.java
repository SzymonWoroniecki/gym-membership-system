package io.github.SzymonWoroniecki.gym_membership_system.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long id) {
        super("Member not found with id: " + id);
    }
}
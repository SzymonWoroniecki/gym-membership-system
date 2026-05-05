package io.github.SzymonWoroniecki.gym_membership_system.exception;

public class MemberAlreadyCancelledException extends RuntimeException {
    public MemberAlreadyCancelledException(Long id) {
        super("Member with id " + id + " is already cancelled");
    }
}
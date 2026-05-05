package io.github.SzymonWoroniecki.gym_membership_system.exception;

public class GymNotFoundException extends RuntimeException {
    public GymNotFoundException(Long id) {
        super("Gym not found with id: " + id);
    }
}
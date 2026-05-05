package io.github.SzymonWoroniecki.gym_membership_system.exception;

public class GymAlreadyExistsException extends RuntimeException {
    public GymAlreadyExistsException(String name) {
        super("Gym with name '" + name + "' already exists");
    }
}
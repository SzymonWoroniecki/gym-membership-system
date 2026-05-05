package io.github.SzymonWoroniecki.gym_membership_system.exception;

public class PlanCapacityReachedException extends RuntimeException {
    public PlanCapacityReachedException(long current, int max) {
        super("Plan capacity reached: " + current + " of " + max + " members");
    }
}
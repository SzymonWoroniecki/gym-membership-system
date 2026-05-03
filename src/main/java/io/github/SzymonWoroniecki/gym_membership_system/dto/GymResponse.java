package io.github.SzymonWoroniecki.gym_membership_system.dto;

public record GymResponse(
        Long id,
        String name,
        String address,
        String phoneNumber
) {
}

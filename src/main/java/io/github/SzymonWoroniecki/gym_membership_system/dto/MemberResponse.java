package io.github.SzymonWoroniecki.gym_membership_system.dto;

import io.github.SzymonWoroniecki.gym_membership_system.entity.MembershipPlan;
import io.github.SzymonWoroniecki.gym_membership_system.enums.MembershipStatus;

import java.time.LocalDate;

public record MemberResponse(
        Long id,
        String fullName,
        String email,
        LocalDate membershipStartDate,
        MembershipStatus status,
        Long planId,
        String planName,
        Long gymId,
        String gymName

) {
}

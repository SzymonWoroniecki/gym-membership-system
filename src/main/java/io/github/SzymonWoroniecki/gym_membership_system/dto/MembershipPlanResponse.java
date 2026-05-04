package io.github.SzymonWoroniecki.gym_membership_system.dto;

import io.github.SzymonWoroniecki.gym_membership_system.enums.PlanType;

import java.math.BigDecimal;

public record MembershipPlanResponse(
        Long id,
        String name,
        PlanType type,
        BigDecimal amount,
        String currency,
        Integer durationInMonths,
        Integer maxMembers,
        Long gymId
) {
}

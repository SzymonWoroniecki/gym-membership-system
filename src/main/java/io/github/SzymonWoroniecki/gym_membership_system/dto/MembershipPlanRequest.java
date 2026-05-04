package io.github.SzymonWoroniecki.gym_membership_system.dto;

import io.github.SzymonWoroniecki.gym_membership_system.enums.PlanType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record MembershipPlanRequest(

        @NotBlank(message = "Name is required")
        @Size(max=100, message = "Name must not exceed 100 characters")
        String name,

        @NotNull(message = "Plan type is required")
        PlanType type,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.00", message = "Amount cannot be negative")
        BigDecimal amount,

        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency code must be 3 characters (ISO 4217)")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be uppercase letters")
        String currency,

        @NotNull(message = "Duration is required")
        @Min(value = 1, message = "Duration must be at least 1 month")
        Integer durationInMonths,

        @NotNull(message = "Max members is required")
        @Min(value = 1, message = "Max members must be at least 1")
        Integer maxMembers
) {
}

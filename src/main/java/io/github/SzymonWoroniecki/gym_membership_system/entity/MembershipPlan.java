package io.github.SzymonWoroniecki.gym_membership_system.entity;

import io.github.SzymonWoroniecki.gym_membership_system.enums.PlanType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "membership_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max=100, message = "Name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "Plan type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlanType type;

    @NotNull(message = "Price is required")
    @Valid
    @Embedded
    private Money price;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 month")
    @Column(nullable = false)
    private Integer durationInMonths;

    @NotNull(message = "Max members is required")
    @Min(value = 1, message = "Max members must be at least 1")
    @Column(nullable = false)
    private Integer maxMembers;

    @NotNull(message = "Gym is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;
}

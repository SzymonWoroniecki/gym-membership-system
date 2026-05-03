package io.github.SzymonWoroniecki.gym_membership_system.repository;

import io.github.SzymonWoroniecki.gym_membership_system.entity.Member;
import io.github.SzymonWoroniecki.gym_membership_system.enums.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    long countByPlanIdAndStatus(Long planId, MembershipStatus status);

    boolean existsByEmailIgnoreCase(String email);
}

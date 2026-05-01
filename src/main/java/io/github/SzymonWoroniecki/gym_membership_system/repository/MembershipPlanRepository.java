package io.github.SzymonWoroniecki.gym_membership_system.repository;

import io.github.SzymonWoroniecki.gym_membership_system.entity.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {

    List<MembershipPlan> findByGymId(Long gymId);
}

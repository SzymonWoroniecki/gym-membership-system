package io.github.SzymonWoroniecki.gym_membership_system.repository;

import io.github.SzymonWoroniecki.gym_membership_system.dto.RevenueReportItem;
import io.github.SzymonWoroniecki.gym_membership_system.entity.Member;
import io.github.SzymonWoroniecki.gym_membership_system.enums.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    long countByPlanIdAndStatus(Long planId, MembershipStatus status);

    boolean existsByEmailIgnoreCase(String email);

    @Query("""
            SELECT new io.github.SzymonWoroniecki.gym_membership_system.dto.RevenueReportItem(
                m.plan.gym.id,
                m.plan.gym.name,
                m.plan.price.currency,
                SUM(m.plan.price.amount)
            )
            FROM Member m
            WHERE m.status = io.github.SzymonWoroniecki.gym_membership_system.enums.MembershipStatus.ACTIVE
            GROUP BY m.plan.gym.id, m.plan.gym.name, m.plan.price.currency
            ORDER BY m.plan.gym.id, m.plan.price.currency
            """)
    List<RevenueReportItem> calculateRevenueReport();
}

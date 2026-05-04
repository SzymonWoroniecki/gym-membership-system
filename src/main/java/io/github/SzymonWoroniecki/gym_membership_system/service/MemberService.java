package io.github.SzymonWoroniecki.gym_membership_system.service;


import io.github.SzymonWoroniecki.gym_membership_system.dto.MemberRequest;
import io.github.SzymonWoroniecki.gym_membership_system.dto.MemberResponse;
import io.github.SzymonWoroniecki.gym_membership_system.entity.Member;
import io.github.SzymonWoroniecki.gym_membership_system.entity.MembershipPlan;
import io.github.SzymonWoroniecki.gym_membership_system.enums.MembershipStatus;
import io.github.SzymonWoroniecki.gym_membership_system.repository.MemberRepository;
import io.github.SzymonWoroniecki.gym_membership_system.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MembershipPlanRepository planRepository;

    @Transactional
    public MemberResponse register(Long planId, MemberRequest request){
        // 1. Pobranie planu - jeśli nie istnieje, rzuca wyjątek
        MembershipPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + planId));
        // 2. Capacity check
        long activeCount = memberRepository.countByPlanIdAndStatus(planId, MembershipStatus.ACTIVE);
        if (activeCount >= plan.getMaxMembers()){
            throw new IllegalStateException(
                    "Plan capacity reached: " + activeCount + " of " + plan.getMaxMembers() + " members"
            );
        }

        // 3. Sprawdzanie unikalności emaila
        if (memberRepository.existsByEmailIgnoreCase(request.email())){
            throw new IllegalStateException("Member with email '" + request.email() + "' already exists");
        }

        // 4. Stwarzanie nowego członka
        Member member = new Member();
        member.setFullName(request.fullName());
        member.setEmail(request.email());
        member.setPlan(plan);
        member.setMembershipStartDate(LocalDate.now());
        member.setStatus(MembershipStatus.ACTIVE);

        Member saved = memberRepository.save(member);
        return mapToResponse(saved);
    }

    @Transactional
    public MemberResponse cancel(Long memberId){
        // 1. Pobranie Członka
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        // 2. Sprawdzanie czy już nie jest anulowany
        if (member.getStatus() == MembershipStatus.CANCELLED) {
            throw new IllegalStateException("Member is already cancelled");
        }

        // 3. Zmiana statusu
        member.setStatus(MembershipStatus.CANCELLED);
        Member saved = memberRepository.save(member);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MemberResponse mapToResponse(Member member) {
        MembershipPlan plan = member.getPlan();
        return new MemberResponse(
                member.getId(),
                member.getFullName(),
                member.getEmail(),
                member.getMembershipStartDate(),
                member.getStatus(),
                plan.getId(),
                plan.getName(),
                plan.getGym().getId(),
                plan.getGym().getName()
        );
    }
}

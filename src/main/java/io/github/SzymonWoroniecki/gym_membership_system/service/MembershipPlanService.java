package io.github.SzymonWoroniecki.gym_membership_system.service;

import io.github.SzymonWoroniecki.gym_membership_system.dto.MembershipPlanRequest;
import io.github.SzymonWoroniecki.gym_membership_system.dto.MembershipPlanResponse;
import io.github.SzymonWoroniecki.gym_membership_system.entity.Gym;
import io.github.SzymonWoroniecki.gym_membership_system.entity.MembershipPlan;
import io.github.SzymonWoroniecki.gym_membership_system.entity.Money;
import io.github.SzymonWoroniecki.gym_membership_system.exception.GymNotFoundException;
import io.github.SzymonWoroniecki.gym_membership_system.repository.GymRepository;

import io.github.SzymonWoroniecki.gym_membership_system.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipPlanService {

    private final MembershipPlanRepository planRepository;
    private final GymRepository gymRepository;

    @Transactional
    public MembershipPlanResponse createPlan(Long gymId, MembershipPlanRequest request){
        // 1. Sprawdź czy siłownia istnieje
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new GymNotFoundException(gymId));

        // 2. Mapowanie DTO -> Entity
        MembershipPlan plan = new MembershipPlan();
        plan.setName(request.name());
        plan.setType(request.type());
        plan.setPrice(new Money(request.amount(), Currency.getInstance(request.currency())));
        plan.setDurationInMonths(request.durationInMonths());
        plan.setMaxMembers(request.maxMembers());
        plan.setGym(gym);

        // 3. Zapis
        MembershipPlan saved = planRepository.save(plan);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MembershipPlanResponse> getPlansByGym(Long gymId){
        // Sprawdź czy siłownia istnieje
        if (!gymRepository.existsById(gymId)){
            throw new GymNotFoundException(gymId);
        }
        return planRepository.findByGymId(gymId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MembershipPlanResponse mapToResponse(MembershipPlan plan){
        return new MembershipPlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getType(),
                plan.getPrice().getAmount(),
                plan.getPrice().getCurrency().getCurrencyCode(),
                plan.getDurationInMonths(),
                plan.getMaxMembers(),
                plan.getGym().getId()
        );
    }
}

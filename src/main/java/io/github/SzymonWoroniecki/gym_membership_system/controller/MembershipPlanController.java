package io.github.SzymonWoroniecki.gym_membership_system.controller;

import io.github.SzymonWoroniecki.gym_membership_system.dto.MembershipPlanRequest;
import io.github.SzymonWoroniecki.gym_membership_system.dto.MembershipPlanResponse;
import io.github.SzymonWoroniecki.gym_membership_system.service.MembershipPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gyms/{gymId}/plans")
@RequiredArgsConstructor
public class MembershipPlanController {

    private final MembershipPlanService planService;

    @PostMapping
    public ResponseEntity<MembershipPlanResponse> createPlan(
            @PathVariable Long gymId,
            @Valid @RequestBody MembershipPlanRequest request){
        MembershipPlanResponse response = planService.createPlan(gymId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MembershipPlanResponse>> getPlansByGym(@PathVariable Long gymId){
        return ResponseEntity.ok(planService.getPlansByGym(gymId));
    }
}

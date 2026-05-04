package io.github.SzymonWoroniecki.gym_membership_system.controller;


import io.github.SzymonWoroniecki.gym_membership_system.dto.MemberRequest;
import io.github.SzymonWoroniecki.gym_membership_system.dto.MemberResponse;
import io.github.SzymonWoroniecki.gym_membership_system.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/plans/{planId}/members")
    public ResponseEntity<MemberResponse> register(
            @PathVariable Long planId,
            @Valid @RequestBody MemberRequest request) {
        MemberResponse response = memberService.register(planId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @PostMapping("/members/{memberId}/cancel")
    public ResponseEntity<MemberResponse> cancel(@PathVariable Long memberId) {
        MemberResponse response = memberService.cancel(memberId);
        return ResponseEntity.ok(response);
    }

}

package io.github.SzymonWoroniecki.gym_membership_system.controller;

import io.github.SzymonWoroniecki.gym_membership_system.dto.GymRequest;
import io.github.SzymonWoroniecki.gym_membership_system.dto.GymResponse;
import io.github.SzymonWoroniecki.gym_membership_system.service.GymService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gyms")
@RequiredArgsConstructor
public class GymController {

    private final GymService gymService;

    @PostMapping
    public ResponseEntity<GymResponse> createGym(@Valid @RequestBody GymRequest request){
        GymResponse response = gymService.createGym(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<GymResponse>> getAllGyms(){
        return ResponseEntity.ok(gymService.getAllGyms());
    }
}

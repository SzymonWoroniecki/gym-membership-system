package io.github.SzymonWoroniecki.gym_membership_system.service;

import io.github.SzymonWoroniecki.gym_membership_system.dto.GymRequest;
import io.github.SzymonWoroniecki.gym_membership_system.dto.GymResponse;
import io.github.SzymonWoroniecki.gym_membership_system.entity.Gym;
import io.github.SzymonWoroniecki.gym_membership_system.exception.GymAlreadyExistsException;
import io.github.SzymonWoroniecki.gym_membership_system.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GymService {

    private final GymRepository gymRepository;

    @Transactional
    public GymResponse createGym(GymRequest request){

        // 1. Walidacja biznesowa: czy nazwa już istnieje (case-insensitive)
        if (gymRepository.existsByNameIgnoreCase(request.name())){
            throw new GymAlreadyExistsException(request.name());
        }

        // 2. Mapowanie DTO -> Entity
        Gym gym = new Gym();
        gym.setName(request.name());
        gym.setAddress(request.address());
        gym.setPhoneNumber(request.phoneNumber());

        // 3. Zapis
        Gym saved = gymRepository.save(gym);

        // 4. Mapowanie Entity -> DTO
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<GymResponse> getAllGyms(){
        return gymRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private GymResponse mapToResponse(Gym gym) {
        return new GymResponse(
                gym.getId(),
                gym.getName(),
                gym.getAddress(),
                gym.getPhoneNumber()
        );
    }

}

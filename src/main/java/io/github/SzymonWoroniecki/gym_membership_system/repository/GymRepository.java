package io.github.SzymonWoroniecki.gym_membership_system.repository;


import io.github.SzymonWoroniecki.gym_membership_system.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {

    boolean existsByNameIgnoreCase(String name);
}

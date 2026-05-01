package io.github.SzymonWoroniecki.gym_membership_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gym")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max=100, message = "Name must not exceed 100 characters")
    @Column(unique=true, nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Address is required")
    @Size(max=150, message = "Address must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String address;

    @NotBlank(message = "Phone number is required")
    @Size(max=20, message = "Phone number must not exceed 20 characters")
    @Pattern(regexp = "^[+]?[0-9\\s-]{7,20}$", message = "Invalid phone number format")
    @Column(nullable = false, length = 20)
    private String phoneNumber;
}

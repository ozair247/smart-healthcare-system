package com.smarthealth.healthcarebackend.entity;

import com.smarthealth.healthcarebackend.enums.Role;
import com.smarthealth.healthcarebackend.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 20)
    private String username;

    @NotBlank @Size(max = 50) @Email
    private String email;

    @NotBlank @Size(max = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;

    private boolean enabled = true;

    private String fullName;
    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
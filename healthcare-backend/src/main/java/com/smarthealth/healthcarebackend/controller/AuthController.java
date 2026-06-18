package com.smarthealth.healthcarebackend.controller;

import com.smarthealth.healthcarebackend.dto.request.LoginRequest;
import com.smarthealth.healthcarebackend.dto.request.SignupRequest;
import com.smarthealth.healthcarebackend.dto.response.JwtResponse;
import com.smarthealth.healthcarebackend.dto.response.MessageResponse;
import com.smarthealth.healthcarebackend.entity.*;
import com.smarthealth.healthcarebackend.enums.Role;
import com.smarthealth.healthcarebackend.repository.*;
import com.smarthealth.healthcarebackend.security.jwt.JwtUtils;
import com.smarthealth.healthcarebackend.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserRepository userRepository;
    @Autowired PatientRepository patientRepository;
    @Autowired DoctorRepository doctorRepository;
    @Autowired ReceptionistRepository receptionistRepository;
    @Autowired PasswordEncoder encoder;
    @Autowired JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority(); // e.g., "ROLE_ADMIN"
        String jwt = jwtUtils.generateJwtToken(authentication.getName(), role.replace("ROLE_", ""));

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(),
                role));   // note: the JwtResponse still expects the full authority string, that's fine
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        Role role = Role.valueOf(signUpRequest.getRole().toUpperCase());
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .role(role)
                .fullName(signUpRequest.getFullName())
                .phone(signUpRequest.getPhone())
                .address(signUpRequest.getAddress())
                .enabled(true)
                .build();
        userRepository.save(user);

        if (role == Role.PATIENT) {
            Patient patient = Patient.builder()
                    .user(user)
                    .dateOfBirth(signUpRequest.getDateOfBirth())
                    .bloodGroup(signUpRequest.getBloodGroup())
                    .build();
            patientRepository.save(patient);
        } else if (role == Role.DOCTOR) {
            Doctor doctor = Doctor.builder()
                    .user(user)
                    .specialization(signUpRequest.getSpecialization())
                    .licenseNumber(signUpRequest.getLicenseNumber())
                    .experienceYears(signUpRequest.getExperienceYears())
                    .available(true)
                    .build();
            doctorRepository.save(doctor);
        } else if (role == Role.RECEPTIONIST) {
            Receptionist receptionist = Receptionist.builder()
                    .user(user)
                    .employeeId(signUpRequest.getEmployeeId())
                    .build();
            receptionistRepository.save(receptionist);
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
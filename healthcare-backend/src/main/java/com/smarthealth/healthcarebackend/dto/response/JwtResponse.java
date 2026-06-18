package com.smarthealth.healthcarebackend.dto.response;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;
}
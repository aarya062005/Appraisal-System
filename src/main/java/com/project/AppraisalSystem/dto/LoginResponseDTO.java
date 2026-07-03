package com.project.AppraisalSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String token;
}
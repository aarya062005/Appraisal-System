package com.project.AppraisalSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String role;
    private String email;
    private String phone;
    private String designation;
    private Long managerId;
    private Long deptId;
    private String managerName;
    private String deptName;
    private Boolean isActive;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;
}

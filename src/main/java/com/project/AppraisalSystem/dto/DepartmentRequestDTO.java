package com.project.AppraisalSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDTO {
    @NotBlank(message = "Department name is required")
    private String deptName;

    private String deptDescription;
}

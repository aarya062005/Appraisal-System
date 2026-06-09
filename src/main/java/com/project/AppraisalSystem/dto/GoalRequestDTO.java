package com.project.AppraisalSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalRequestDTO {
    private Long appraisalId;
    private Long employeeId;
    private String title;
    private String description;
    private LocalDate dueDate;
}

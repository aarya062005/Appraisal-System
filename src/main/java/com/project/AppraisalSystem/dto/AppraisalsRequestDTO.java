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
public class AppraisalsRequestDTO {
    private String cycleName;
    private LocalDate cycleStartDate;
    private LocalDate cycleEndDate;
    private Long employeeId;
    private Long managerId;
}

package com.project.AppraisalSystem.dto;



import com.project.AppraisalSystem.entity.enums.AppraisalStatus;
import com.project.AppraisalSystem.entity.enums.CycleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppraisalsByManagerDTO {
    private Long appraisalId;
    private String cycleName;
    private LocalDate cycleStartDate;
    private LocalDate cycleEndDate;
    private CycleStatus cycleStatus;
    private String employeeEmail;
    private AppraisalStatus appraisalStatus;
    private LocalDateTime createdAt;
}
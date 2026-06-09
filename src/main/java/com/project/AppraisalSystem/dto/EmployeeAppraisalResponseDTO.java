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

public class EmployeeAppraisalResponseDTO {

    private Long appraisalId;
    private String cycleName;
    private LocalDate cycleStartDate;
    private LocalDate cycleEndDate;
    private CycleStatus cycleStatus;
    private String managerEmail;
    private String managerName;
    private String whatWentWell;
    private String whatToImprove;
    private String achievements;
    private Integer selfRating;
    private String managerStrengths;
    private String managerImprove;
    private String managerComments;
    private Integer managerRating;
    private AppraisalStatus appraisalStatus;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
}


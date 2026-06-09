package com.project.AppraisalSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerAppraisalResponseDTO {
    private Long appraisalId;
    private String cycleName;
    private LocalDate cycleStartDate;
    private LocalDate cycleEndDate;
    private CycleStatus cycleStatus;
    private String employeeEmail;
    private String employeeName;
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


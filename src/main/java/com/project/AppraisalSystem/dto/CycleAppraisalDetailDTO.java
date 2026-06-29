package com.project.AppraisalSystem.dto;

import com.project.AppraisalSystem.entity.enums.AppraisalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CycleAppraisalDetailDTO {
    private Long appraisalId;
    private String employeeEmail;
    private String managerEmail;
    private String deptName;
    private AppraisalStatus appraisalStatus;
    private Integer selfRating;
    private Integer managerRating;
}
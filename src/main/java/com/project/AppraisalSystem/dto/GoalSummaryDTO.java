package com.project.AppraisalSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.AppraisalSystem.entity.enums.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoalSummaryDTO {
    private Long goalId;
    private Long appraisalId;
    private String employeeEmail;
    private String employeeName;
    private String title;
    private GoalStatus status;
    private LocalDate dueDate;

}

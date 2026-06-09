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
public class GoalResponseDTO {
    private Long goalId;
    private Long appraisalId;
    private Long employeeId;
    private String employeeEmail;
    private String employeeName;
    private String title;
    private String description;
    private Integer progressPercent;
    private GoalStatus status;
    private Boolean employeeCompleted;
    private String employeeNote;
    private LocalDate dueDate;
}
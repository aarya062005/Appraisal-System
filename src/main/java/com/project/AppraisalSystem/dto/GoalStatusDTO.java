package com.project.AppraisalSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.AppraisalSystem.entity.enums.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoalStatusDTO {
    private Long goalId;
    private String title;
    private String employeeEmail;
    private String employeeName;
    private GoalStatus status;
    private Boolean employeeCompleted;
    private String employeeNote;

}

package com.project.AppraisalSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelfAssessmentDTO {
    private String whatWentWell;
    private String whatToImprove;
    private String achievements;
    private Integer selfRating;
}


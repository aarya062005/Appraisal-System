package com.project.AppraisalSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerReviewDTO {
    private String managerStrengths;
    private String managerImprove;
    private String managerComments;
    private Integer managerRating;
}

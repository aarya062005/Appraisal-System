package com.project.AppraisalSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CycleReportDTO {
    private String cycleName;
    private int totalAppraisals;
    private int acknowledged;
    private int completion;
    private int pendingAction;
    private Double avgSelfRating;
    private Double avgManagerRating;
    private Map<String, Long> statusBreakdown;
}
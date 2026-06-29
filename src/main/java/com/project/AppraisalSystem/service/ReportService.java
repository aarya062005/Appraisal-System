package com.project.AppraisalSystem.service;

import com.project.AppraisalSystem.dto.CycleAppraisalDetailDTO;
import com.project.AppraisalSystem.dto.CycleReportDTO;

import java.util.List;

public interface ReportService {
    List<String> getAllCycles();
    CycleReportDTO getCycleReport(String cycleName);
    List<CycleAppraisalDetailDTO> getCycleAppraisalDetails(String cycleName);
}
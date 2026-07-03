package com.project.AppraisalSystem.service;

import com.project.AppraisalSystem.dto.CycleDetailDTO;
import com.project.AppraisalSystem.dto.CycleReportDTO;

import java.util.List;

public interface ReportService {
    List<String> findAllCycleNames();
    CycleReportDTO getCycleReport(String cycleName);
    List<CycleDetailDTO> getCycleDetails(String cycleName);
}
package com.project.AppraisalSystem.controller;

import com.project.AppraisalSystem.dto.CycleAppraisalDetailDTO;
import com.project.AppraisalSystem.dto.CycleReportDTO;
import com.project.AppraisalSystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/cycles")
    public ResponseEntity<List<String>> getAllCycles() {
        return ResponseEntity.ok(reportService.getAllCycles());
    }

    @GetMapping("/cycle/{cycleName}")
    public ResponseEntity<CycleReportDTO> getCycleReport(@PathVariable String cycleName) {
        return ResponseEntity.ok(reportService.getCycleReport(cycleName));
    }

    @GetMapping("/cycle/{cycleName}/details")
    public ResponseEntity<List<CycleAppraisalDetailDTO>> getCycleDetails(@PathVariable String cycleName) {
        return ResponseEntity.ok(reportService.getCycleAppraisalDetails(cycleName));
    }
}
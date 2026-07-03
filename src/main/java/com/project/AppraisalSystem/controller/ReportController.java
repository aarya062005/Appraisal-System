package com.project.AppraisalSystem.controller;

import com.project.AppraisalSystem.dto.CycleDetailDTO;
import com.project.AppraisalSystem.dto.CycleReportDTO;
import com.project.AppraisalSystem.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/cycles")
    public ResponseEntity<List<String>> findAllCycleNames() {
        return ResponseEntity.ok(reportService.findAllCycleNames());
    }

    @GetMapping("/cycle/{cycleName}")
    public ResponseEntity<CycleReportDTO> getCycleReport(@PathVariable String cycleName) {
        return ResponseEntity.ok(reportService.getCycleReport(cycleName));
    }

    @GetMapping("/cycle/{cycleName}/details")
    public ResponseEntity<List<CycleDetailDTO>> getCycleDetails(@PathVariable String cycleName) {
        return ResponseEntity.ok(reportService.getCycleDetails(cycleName));
    }
}
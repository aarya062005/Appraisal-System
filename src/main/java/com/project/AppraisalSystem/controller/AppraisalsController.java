
package com.project.AppraisalSystem.controller;
import com.project.AppraisalSystem.dto.*;
import com.project.AppraisalSystem.entity.enums.AppraisalStatus;
import com.project.AppraisalSystem.entity.enums.CycleStatus;
import com.project.AppraisalSystem.service.AppraisalsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appraisals")
@AllArgsConstructor
public class AppraisalsController {

    private final AppraisalsService appraisalsService;

    @GetMapping
    public ResponseEntity<List<AppraisalsSummaryDTO>> findAllAppraisals() {
        return ResponseEntity.ok(appraisalsService.findAllAppraisals());
    }

    @GetMapping("/{appraisalId}")
    public ResponseEntity<AppraisalsSummaryDTO> findAppraisalById(@PathVariable Long appraisalId) {
        return ResponseEntity.ok(appraisalsService.findAppraisalById(appraisalId));
    }

    @GetMapping("/cycle/{cycleName}")
    public ResponseEntity<List<AppraisalsSummaryDTO>> findAppraisalsByCycle(
            @PathVariable String cycleName) {
        return ResponseEntity.ok(appraisalsService.findAppraisalsByCycle(cycleName));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppraisalsSummaryDTO>> findAppraisalsByStatus(
            @PathVariable AppraisalStatus status) {
        return ResponseEntity.ok(appraisalsService.findAppraisalsByStatus(status));
    }

    @GetMapping("/cycle-status/{cycleStatus}")
    public ResponseEntity<List<AppraisalsSummaryDTO>> findAppraisalsByCycleStatus(
            @PathVariable CycleStatus cycleStatus) {
        return ResponseEntity.ok(appraisalsService.findAppraisalsByCycleStatus(cycleStatus));
    }
    @GetMapping("/{appraisalId}/employee-view")
    public ResponseEntity<EmployeeAppraisalResponseDTO> findAppraisalByIdForEmployee(
            @PathVariable Long appraisalId) {
        return ResponseEntity.ok(appraisalsService.findAppraisalByIdForEmployee(appraisalId));
    }


    @PostMapping
    public ResponseEntity<AppraisalsSummaryDTO> createAppraisal(
            @RequestBody AppraisalsRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appraisalsService.createAppraisal(dto));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<AppraisalsSummaryDTO> approveAppraisal(@PathVariable Long id) {
        return ResponseEntity.ok(appraisalsService.approveAppraisal(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppraisal(@PathVariable Long id) {
        return ResponseEntity.ok(appraisalsService.deleteAppraisal(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AppraisalsByEmployeeDTO>> findAppraisalsByEmployee(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(appraisalsService.findAppraisalsByEmployee_Id(employeeId));
    }

    @GetMapping("/employee/email/{employeeEmail}")
    public ResponseEntity<List<AppraisalsByEmployeeDTO>> findAppraisalsByEmployeeEmail(
            @PathVariable String employeeEmail) {
        return ResponseEntity.ok(appraisalsService.findAppraisalsByEmployeeEmail(employeeEmail));
    }

    @PutMapping("/{id}/self-assessment/draft")
    public ResponseEntity<EmployeeAppraisalResponseDTO> saveSelfAssessmentDraft(
            @PathVariable Long id,
            @RequestBody SelfAssessmentDTO dto) {
        return ResponseEntity.ok(appraisalsService.saveSelfAssessmentDraft(id, dto));
    }

    @PutMapping("/self-assessment/draft/email")
    public ResponseEntity<EmployeeAppraisalResponseDTO> saveSelfAssessmentDraftByEmail(
            @RequestParam String employeeEmail,
            @RequestParam String cycleName,
            @RequestBody SelfAssessmentDTO dto) {
        return ResponseEntity.ok(appraisalsService
                .saveSelfAssessmentDraftByEmployeeEmail(employeeEmail, cycleName, dto));
    }

    @PutMapping("/{id}/self-assessment/submit")
    public ResponseEntity<EmployeeAppraisalResponseDTO> submitSelfAssessment(
            @PathVariable Long id,
            @RequestBody SelfAssessmentDTO dto) {
        return ResponseEntity.ok(appraisalsService.submitSelfAssessment(id, dto));
    }

    @PutMapping("/self-assessment/submit/email")
    public ResponseEntity<EmployeeAppraisalResponseDTO> submitSelfAssessmentByEmail(
            @RequestParam String employeeEmail,
            @RequestParam String cycleName,
            @RequestBody SelfAssessmentDTO dto) {
        return ResponseEntity.ok(appraisalsService
                .submitSelfAssessmentByEmployeeEmail(employeeEmail, cycleName, dto));
    }

    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<EmployeeAppraisalResponseDTO> acknowledgeAppraisal(
            @PathVariable Long id) {
        return ResponseEntity.ok(appraisalsService.acknowledgeAppraisal(id));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<AppraisalsByManagerDTO>> findAppraisalsByManager(
            @PathVariable Long managerId) {
        return ResponseEntity.ok(appraisalsService.findAppraisalsByManager_Id(managerId));
    }

    @GetMapping("/manager/email/{managerEmail}")
    public ResponseEntity<List<AppraisalsByManagerDTO>> findAppraisalsByManagerEmail(
            @PathVariable String managerEmail) {
        return ResponseEntity.ok(appraisalsService.findAppraisalsByManagerEmail(managerEmail));
    }

    @PutMapping("/{id}/manager-review/draft")
    public ResponseEntity<ManagerAppraisalResponseDTO> saveManagerReviewDraft(
            @PathVariable Long id,
            @RequestBody ManagerReviewDTO dto) {
        return ResponseEntity.ok(appraisalsService.saveManagerReviewDraft(id, dto));
    }

    @PutMapping("/manager-review/draft/email")
    public ResponseEntity<ManagerAppraisalResponseDTO> saveManagerReviewDraftByEmail(
            @RequestParam String employeeEmail,
            @RequestParam String cycleName,
            @RequestBody ManagerReviewDTO dto) {
        return ResponseEntity.ok(appraisalsService
                .saveManagerReviewDraftByEmployeeEmail(employeeEmail, cycleName, dto));
    }

    @PutMapping("/{id}/manager-review/submit")
    public ResponseEntity<ManagerAppraisalResponseDTO> submitManagerReview(
            @PathVariable Long id,
            @RequestBody ManagerReviewDTO dto) {
        return ResponseEntity.ok(appraisalsService.submitManagerReview(id, dto));
    }

    @PutMapping("/manager-review/submit/email")
    public ResponseEntity<ManagerAppraisalResponseDTO> submitManagerReviewByEmail(
            @RequestParam String employeeEmail,
            @RequestParam String cycleName,
            @RequestBody ManagerReviewDTO dto) {
        return ResponseEntity.ok(appraisalsService
                .submitManagerReviewByEmployeeEmail(employeeEmail, cycleName, dto));
    }
}
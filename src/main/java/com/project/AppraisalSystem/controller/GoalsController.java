
package com.project.AppraisalSystem.controller;
import com.project.AppraisalSystem.dto.GoalRequestDTO;
import com.project.AppraisalSystem.dto.GoalResponseDTO;
import com.project.AppraisalSystem.dto.GoalStatusDTO;
import com.project.AppraisalSystem.dto.GoalSummaryDTO;
import com.project.AppraisalSystem.entity.enums.GoalStatus;
import com.project.AppraisalSystem.service.GoalsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@AllArgsConstructor
public class GoalsController {

    private final GoalsService goalsService;

    @GetMapping
    public ResponseEntity<List<GoalSummaryDTO>> findAllGoals() {
        return ResponseEntity.ok(goalsService.findAllGoals());
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<GoalResponseDTO> findGoalById(@PathVariable Long goalId) {
        return ResponseEntity.ok(goalsService.findGoalById(goalId));
    }

    @GetMapping("/appraisal/{appraisalId}")
    public ResponseEntity<List<GoalSummaryDTO>> findGoalsByAppraisal(
            @PathVariable Long appraisalId) {
        return ResponseEntity.ok(goalsService.findGoalsByAppraisal(appraisalId));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<GoalResponseDTO>> findGoalsByEmployee(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(goalsService.findGoalsByEmployee(employeeId));
    }

    @GetMapping("/appraisal/{appraisalId}/employee/{employeeId}")
    public ResponseEntity<List<GoalResponseDTO>> findGoalsByAppraisalAndEmployee(
            @PathVariable Long appraisalId,
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(goalsService.findGoalsByAppraisalAndEmployee(appraisalId, employeeId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<GoalSummaryDTO>> findGoalsByStatus(
            @PathVariable GoalStatus status) {
        return ResponseEntity.ok(goalsService.findGoalsByStatus(status));
    }

    @GetMapping("/employee/{employeeId}/status/{status}")
    public ResponseEntity<List<GoalResponseDTO>> findGoalsByEmployeeAndStatus(
            @PathVariable Long employeeId,
            @PathVariable GoalStatus status) {
        return ResponseEntity.ok(goalsService.findGoalsByEmployeeAndStatus(employeeId, status));
    }

    @GetMapping("/employee/{employeeId}/completed/{completed}")
    public ResponseEntity<List<GoalResponseDTO>> findGoalsByEmployeeCompleted(
            @PathVariable Long employeeId,
            @PathVariable Boolean completed) {
        return ResponseEntity.ok(goalsService.findGoalsByEmployeeCompleted(employeeId, completed));
    }

    @GetMapping("/appraisal/{appraisalId}/completed/{completed}")
    public ResponseEntity<List<GoalResponseDTO>> findGoalsByAppraisalAndEmployeeCompleted(
            @PathVariable Long appraisalId,
            @PathVariable Boolean completed) {
        return ResponseEntity.ok(goalsService.findGoalsByAppraisalAndEmployeeCompleted(appraisalId, completed));
    }

    @PostMapping
    public ResponseEntity<GoalResponseDTO> createGoal(@RequestBody GoalRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(goalsService.createGoal(dto));
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<GoalResponseDTO> updateGoal(
            @PathVariable Long goalId,
            @RequestBody GoalRequestDTO dto) {
        return ResponseEntity.ok(goalsService.updateGoal(goalId, dto));
    }

    @PatchMapping("/{goalId}/cancel")
    public ResponseEntity<GoalResponseDTO> cancelGoal(@PathVariable Long goalId) {
        return ResponseEntity.ok(goalsService.cancelGoal(goalId));
    }

    @PatchMapping("/{goalId}/confirm")
    public ResponseEntity<GoalStatusDTO> confirmGoalCompletion(
            @PathVariable Long goalId,
            @RequestParam GoalStatus status) {
        return ResponseEntity.ok(goalsService.confirmGoalCompletion(goalId, status));
    }

    @PatchMapping("/{goalId}/submit")
    public ResponseEntity<GoalStatusDTO> submitGoalCompletion(
            @PathVariable Long goalId,
            @RequestParam Boolean completed,
            @RequestParam(required = false) String note) {
        return ResponseEntity.ok(goalsService.submitGoalCompletion(goalId, completed, note));
    }

    @PatchMapping("/{goalId}/note")
    public ResponseEntity<GoalStatusDTO> updateEmployeeNote(
            @PathVariable Long goalId,
            @RequestParam String note) {
        return ResponseEntity.ok(goalsService.updateEmployeeNote(goalId, note));
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<String> deleteGoal(@PathVariable Long goalId) {
        return ResponseEntity.ok(goalsService.deleteGoal(goalId));
    }
}
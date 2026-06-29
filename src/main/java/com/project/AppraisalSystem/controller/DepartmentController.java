package com.project.AppraisalSystem.controller;

import com.project.AppraisalSystem.dto.DepartmentRequestDTO;
import com.project.AppraisalSystem.dto.DepartmentResponseDTO;
import com.project.AppraisalSystem.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;


    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> findAll() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.findById(id));
    }

    @GetMapping("/name/{deptName}")
    public ResponseEntity<DepartmentResponseDTO> findByName(@PathVariable String deptName) {
        return ResponseEntity.ok(departmentService.findByName(deptName));
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> addDepartment(
            @Valid @RequestBody DepartmentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentService.addDepartment(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDTO dto) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> patchDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentRequestDTO dto) {
        return ResponseEntity.ok(departmentService.patchDepartment(id, dto));
    }
}
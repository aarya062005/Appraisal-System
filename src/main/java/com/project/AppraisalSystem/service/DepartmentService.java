package com.project.AppraisalSystem.service;

import com.project.AppraisalSystem.dto.DepartmentRequestDTO;
import com.project.AppraisalSystem.dto.DepartmentResponseDTO;
import java.util.List;

public interface DepartmentService {
    List<DepartmentResponseDTO> findAll();
    DepartmentResponseDTO findById(Long deptId);
    DepartmentResponseDTO findByName(String deptName);
    DepartmentResponseDTO addDepartment(DepartmentRequestDTO dto);
    DepartmentResponseDTO updateDepartment(Long deptId, DepartmentRequestDTO dto);
    boolean deleteDepartment(Long deptId);
    DepartmentResponseDTO patchDepartment(Long deptId, DepartmentRequestDTO dto);
}

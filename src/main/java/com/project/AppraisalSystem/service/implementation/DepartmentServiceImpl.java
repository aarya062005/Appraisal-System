package com.project.AppraisalSystem.service.implementation;

import com.project.AppraisalSystem.dto.DepartmentRequestDTO;
import com.project.AppraisalSystem.dto.DepartmentResponseDTO;
import com.project.AppraisalSystem.entity.Department;
import com.project.AppraisalSystem.repository.Departmentrepository;
import com.project.AppraisalSystem.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final Departmentrepository departmentRepository;
    private final ModelMapper modelMapper;


    public DepartmentServiceImpl(Departmentrepository departmentRepository,
                                 ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }
//----------------------- Get All Department------------------
    @Override
    public List<DepartmentResponseDTO> findAll() {
        return departmentRepository.findAll()
                .stream()
                .sorted((a, b) -> a.getDeptName().compareTo(b.getDeptName()))
                .map(dept -> modelMapper.map(dept, DepartmentResponseDTO.class))
                .collect(Collectors.toList());
    }
// -------------------------Get by ID-------------------------------
    @Override
    public DepartmentResponseDTO findById(Long deptId) {
        Department dept = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException(
                        "Department not found with id: " + deptId));
        return modelMapper.map(dept, DepartmentResponseDTO.class);
    }
//-----------------------Get by department name----------------
    @Override
    public DepartmentResponseDTO findByName(String deptName) {
        Department dept = departmentRepository.findByDeptName(deptName)
                .orElseThrow(() -> new RuntimeException(
                        "Department not found with name: " + deptName));
        return modelMapper.map(dept, DepartmentResponseDTO.class);
    }
//--------------------------Create DEPARTMENT-----------------------
    @Override
    public DepartmentResponseDTO addDepartment(DepartmentRequestDTO dto) {
        departmentRepository.findByDeptName(dto.getDeptName().trim())
                .ifPresent(existing -> {
                    throw new RuntimeException(
                            "Department already exists: " + dto.getDeptName());
                });
        Department department = modelMapper.map(dto, Department.class);
        department.setDeptName(department.getDeptName().trim());
        return modelMapper.map(departmentRepository.save(department),
                DepartmentResponseDTO.class);
    }
//----------------PUT------------------------------------------------
    @Override
    public DepartmentResponseDTO updateDepartment(Long deptId, DepartmentRequestDTO dto) {
        Department existing = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException(
                        "Department not found with id: " + deptId));

        departmentRepository.findByDeptName(dto.getDeptName().trim())
                .filter(found -> !found.getDeptId().equals(deptId))
                .ifPresent(found -> {
                    throw new RuntimeException(
                            "Department name already taken: " + dto.getDeptName());
                });

        existing.setDeptName(dto.getDeptName().trim());
        existing.setDeptDescription(dto.getDeptDescription());
        return modelMapper.map(departmentRepository.save(existing),
                DepartmentResponseDTO.class);
    }
//---------------DELETE department------------------------------------------
    @Override
    public boolean deleteDepartment(Long deptId) {
        Department existing = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException(
                        "Department not found with id: " + deptId));
        departmentRepository.delete(existing);
        return true;
    }
//----------------------------Patch department------------------------------------
    @Override
    public DepartmentResponseDTO patchDepartment(Long deptId, DepartmentRequestDTO dto) {
        Department existing = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException(
                        "Department not found with id: " + deptId));

        if (dto.getDeptName() != null) {
            String newName = dto.getDeptName().trim();
            departmentRepository.findByDeptName(newName)
                    .filter(found -> !found.getDeptId().equals(deptId))
                    .ifPresent(found -> {
                        throw new RuntimeException(
                                "Department name already taken: " + newName);
                    });
            existing.setDeptName(newName);
        }

        if (dto.getDeptDescription() != null) {
            existing.setDeptDescription(dto.getDeptDescription());
        }

        return modelMapper.map(departmentRepository.save(existing),
                DepartmentResponseDTO.class);
    }
}
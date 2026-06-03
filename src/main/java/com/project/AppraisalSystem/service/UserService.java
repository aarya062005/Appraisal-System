package com.project.AppraisalSystem.service;

import com.project.AppraisalSystem.dto.DepartmentResponseDTO;
import com.project.AppraisalSystem.dto.UserRequestDTO;
import com.project.AppraisalSystem.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
  List<UserResponseDTO> findAllUsers();
  UserResponseDTO findUserById(Long userId);
  UserResponseDTO findUserByEmail(String userEmail);
  List<UserResponseDTO> findAllUsersByDepartmentId(Long departmentId);
  List<UserResponseDTO> findAllUsersByManagerId(Long managerId);
  UserResponseDTO findAllUsersByManger(Long mangerId);
  UserResponseDTO findAllUsersByDeptId(Long deptId);

}

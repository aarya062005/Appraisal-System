package com.project.AppraisalSystem.service;

import com.project.AppraisalSystem.dto.LoginRequestDTO;
import com.project.AppraisalSystem.dto.LoginResponseDTO;
import com.project.AppraisalSystem.dto.UserRequestDTO;
import com.project.AppraisalSystem.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

 // AUTH
 LoginResponseDTO login(LoginRequestDTO dto);

 // FIND
 List<UserResponseDTO> findAllUsers();
 UserResponseDTO findUserById(Long userId);
 UserResponseDTO findUserByEmail(String userEmail);
 List<UserResponseDTO> findAllUsersByDepartment(Long departmentId);
 List<UserResponseDTO> findAllUsersByManager(Long managerId);

 // CREATE
 UserResponseDTO createUser(UserRequestDTO dto);

 // UPDATE
 UserResponseDTO updateFirstName(Long id, String firstName);
 UserResponseDTO updateLastName(Long id, String lastName);
 String updatePassword(Long id, String oldPassword, String newPassword);
 String updateStatus(Long id, Boolean isActive);
 UserResponseDTO updateManager(Long userId, Long managerId);
 String resetPassword(Long id, String newPassword);
 UserResponseDTO updateDesignation(Long id, String designation);

 // DELETE
 String deleteById(Long id);
 String deleteByEmail(String email);
}
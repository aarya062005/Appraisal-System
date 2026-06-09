package com.project.AppraisalSystem.service;

import com.project.AppraisalSystem.dto.DepartmentResponseDTO;
import com.project.AppraisalSystem.dto.UserRequestDTO;
import com.project.AppraisalSystem.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
 //---------------------FIND---------------------------------
  List<UserResponseDTO> findAllUsers();
  UserResponseDTO findUserById(Long userId);
  UserResponseDTO findUserByEmail(String userEmail);
  List<UserResponseDTO> findAllUsersByDepartment(Long departmentId);
  List<UserResponseDTO> findAllUsersByManager(Long managerId);
  //------------------CREATE---------------------------------
  UserResponseDTO createUser(UserRequestDTO dto);
 //------------------UPDATE-----------------------------------
  UserResponseDTO updateFirstName(Long id ,String firstName);
  UserResponseDTO updateLastName(Long id ,String lastName);
  String updatePassword(Long id,String oldPassword ,String newPassword);
String updateStatus(Long id,Boolean isActive);
UserResponseDTO updateManger(Long id , Long mangerId);

 UserResponseDTO updateManager(Long userId, Long managerId);

 String resetPassword(Long id , String newPassword);
UserResponseDTO updateDesignation(Long Id, String designation);
//------------------DELETE------------------------------------------
String deleteById(Long id);
String deleteByEmail(String email);

}

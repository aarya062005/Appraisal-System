package com.project.AppraisalSystem.service.implementation;

import com.project.AppraisalSystem.dto.UserRequestDTO;
import com.project.AppraisalSystem.dto.UserResponseDTO;
import com.project.AppraisalSystem.entity.Department;
import com.project.AppraisalSystem.entity.User;
import com.project.AppraisalSystem.entity.enums.Roles;
import com.project.AppraisalSystem.exception.BadRequestException;
import com.project.AppraisalSystem.exception.DuplicateResourceException;
import com.project.AppraisalSystem.exception.ResourceNotFoundException;
import com.project.AppraisalSystem.repository.DepartmentRepository;
import com.project.AppraisalSystem.repository.UserRepository;
import com.project.AppraisalSystem.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
        if (user.getManager() != null) {
            dto.setManagerId(user.getManager().getUserId());
            dto.setManagerName(user.getManager().getFirstName()
                    + " " + user.getManager().getLastName());
        }
        if (user.getDepartment() != null) {
            dto.setDeptId(user.getDepartment().getDeptId());
            dto.setDeptName(user.getDepartment().getDeptName());
        }
        return dto;
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getUserId() != null)
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO findUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
    }

    @Override
    public UserResponseDTO findUserByEmail(String email) {
        return userRepository.findByEmail(email.trim())
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email));
    }

    @Override
    public List<UserResponseDTO> findAllUsersByDepartment(Long deptId) {
        departmentRepository.findById(deptId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with id: " + deptId));
        return userRepository.findAllByDepartment_DeptId(deptId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> findAllUsersByManager(Long managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Manager not found with id: " + managerId));
        if (!manager.getRole().equals(Roles.MANAGER)) {
            throw new BadRequestException(
                    "User with id: " + managerId + " is not a manager");
        }
        return userRepository.findAllByManager_UserId(managerId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "User already exists with email: " + dto.getEmail());
                });
        User user = modelMapper.map(dto, User.class);
        if (dto.getDeptId() != null) {
            Department department = departmentRepository.findById(dto.getDeptId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department not found with id: " + dto.getDeptId()));
            user.setDepartment(department);
        }
        if (dto.getManagerId() != null) {
            User manager = userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Manager not found with id: " + dto.getManagerId()));
            if (!manager.getRole().equals(Roles.MANAGER)) {
                throw new BadRequestException(
                        "User with id: " + dto.getManagerId() + " is not a manager");
            }
            user.setManager(manager);
        }
        user.setIsActive(true);
        return toResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateFirstName(Long id, String firstName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        user.setFirstName(firstName.trim());
        return toResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateLastName(Long id, String lastName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        user.setLastName(lastName.trim());
        return toResponseDTO(userRepository.save(user));
    }

    @Override
    public String updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        if (!user.getPassword().equals(oldPassword)) {
            throw new BadRequestException("Old password is incorrect");
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return "Password updated successfully";
    }

    @Override
    public String updateStatus(Long id, Boolean isActive) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        user.setIsActive(isActive);
        userRepository.save(user);
        return isActive ? "User activated successfully"
                : "User deactivated successfully";
    }

    @Override
    public UserResponseDTO updateManger(Long id, Long mangerId) {
        return null;
    }

    @Override
    public UserResponseDTO updateManager(Long userId, Long managerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Manager not found with id: " + managerId));
        if (!manager.getRole().equals(Roles.MANAGER)) {
            throw new BadRequestException(
                    "User with id: " + managerId + " is not a manager");
        }
        user.setManager(manager);
        return toResponseDTO(userRepository.save(user));
    }

    @Override
    public String resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        user.setPassword(newPassword);
        userRepository.save(user);
        return "Password reset successfully";
    }

    @Override
    public UserResponseDTO updateDesignation(Long id, String designation) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        user.setDesignation(designation.trim());
        return toResponseDTO(userRepository.save(user));
    }

    @Override
    public String deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        userRepository.delete(user);
        return "User deleted successfully";
    }

    @Override
    public String deleteByEmail(String email) {
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email));
        userRepository.delete(user);
        return "User deleted successfully";
    }
}
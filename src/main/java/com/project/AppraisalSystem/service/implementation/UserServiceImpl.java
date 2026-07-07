package com.project.AppraisalSystem.service.implementation;

import com.project.AppraisalSystem.dto.BulkUploadResultDTO;
import com.project.AppraisalSystem.dto.LoginRequestDTO;
import com.project.AppraisalSystem.dto.LoginResponseDTO;
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
import com.project.AppraisalSystem.security.JwtUtil;
import com.project.AppraisalSystem.service.EmailService;
import com.project.AppraisalSystem.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder; // ← BCrypt
    private final EmailService emailService;

    // ── DTO mapper ────────────────────────────────────────────────────────
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

    // ── AUTH ──────────────────────────────────────────────────────────────
    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()
                || dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BadRequestException("Email and password are required");
        }

        User user = userRepository.findByEmail(dto.getEmail().trim())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (user.getIsActive() == null || !user.getIsActive()) {
            throw new BadRequestException("This account has been deactivated");
        }

        // ── BCrypt password check ─────────────────────────────────────────
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        // ── Generate JWT token ────────────────────────────────────────────
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return LoginResponseDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .build();
    }

    // ── FIND ──────────────────────────────────────────────────────────────
    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
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

    // ── CREATE ────────────────────────────────────────────────────────────
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "User already exists with email: " + dto.getEmail());
                });
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }
        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // ← BCrypt
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

    // ── BULK UPLOAD ───────────────────────────────────────────────────────
    @Override
    public BulkUploadResultDTO bulkUploadUsers(MultipartFile file) {
        int successCount = 0;
        List<BulkUploadResultDTO.RowError> errors = new ArrayList<>();

        List<User> allUsers = userRepository.findAll();
        List<Department> allDepartments = departmentRepository.findAll();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                int rowNumber = i + 1;
                String email = getCellString(row, 2);

                try {
                    String firstName = getCellString(row, 0);
                    String lastName = getCellString(row, 1);
                    String phone = getCellString(row, 3);
                    String roleStr = getCellString(row, 4).toUpperCase();
                    String designation = getCellString(row, 5);
                    String deptName = getCellString(row, 6);
                    String managerName = getCellString(row, 7);

                    if (firstName.isBlank() || lastName.isBlank()
                            || email.isBlank() || roleStr.isBlank()) {
                        throw new BadRequestException("Missing required field(s)");
                    }

                    boolean emailExists = allUsers.stream()
                            .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
                    if (emailExists) {
                        throw new BadRequestException("Duplicate email");
                    }

                    Roles role;
                    try {
                        role = Roles.valueOf(roleStr);
                    } catch (IllegalArgumentException ex) {
                        throw new BadRequestException("Invalid role: " + roleStr);
                    }

                    Department department = null;
                    if (!deptName.isBlank()) {
                        department = allDepartments.stream()
                                .filter(d -> d.getDeptName().equalsIgnoreCase(deptName))
                                .findFirst()
                                .orElseThrow(() -> new BadRequestException(
                                        "Department not found: " + deptName));
                    }

                    User manager = null;
                    if (!managerName.isBlank()) {
                        List<User> matches = allUsers.stream()
                                .filter(u -> (u.getFirstName() + " " + u.getLastName())
                                        .equalsIgnoreCase(managerName.trim()))
                                .toList();
                        if (matches.isEmpty()) {
                            throw new BadRequestException("Manager not found: " + managerName);
                        }
                        if (matches.size() > 1) {
                            throw new BadRequestException(
                                    "Multiple managers named: " + managerName + " — use a unique name");
                        }
                        manager = matches.get(0);
                    }

                    String tempPassword = generateTempPassword();

                    User newUser = new User();
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setEmail(email);
                    newUser.setPhone(phone);
                    newUser.setDesignation(designation);
                    newUser.setRole(role);
                    newUser.setPassword(passwordEncoder.encode(tempPassword));
                    newUser.setIsActive(true);
                    newUser.setDepartment(department);
                    newUser.setManager(manager);

                    User saved = userRepository.save(newUser);
                    allUsers.add(saved); // so later rows in the same file can reference this as a manager

                    emailService.sendWelcomeEmail(email, firstName, tempPassword);

                    successCount++;

                } catch (Exception e) {
                    errors.add(BulkUploadResultDTO.RowError.builder()
                            .rowNumber(rowNumber)
                            .email(email)
                            .reason(e.getMessage())
                            .build());
                }
            }
        } catch (Exception e) {
            throw new BadRequestException("Could not read Excel file: " + e.getMessage());
        }

        return BulkUploadResultDTO.builder()
                .successCount(successCount)
                .failureCount(errors.size())
                .errors(errors)
                .build();
    }

    private String getCellString(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }

    private String generateTempPassword() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // ── UPDATE ────────────────────────────────────────────────────────────
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
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) { // ← BCrypt
            throw new BadRequestException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword)); // ← BCrypt
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
        return isActive ? "User activated successfully" : "User deactivated successfully";
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
        user.setPassword(passwordEncoder.encode(newPassword)); // ← BCrypt
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

    // ── DELETE ────────────────────────────────────────────────────────────
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
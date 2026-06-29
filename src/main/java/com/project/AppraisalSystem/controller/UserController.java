package com.project.AppraisalSystem.controller;

import com.project.AppraisalSystem.dto.UserRequestDTO;
import com.project.AppraisalSystem.dto.UserResponseDTO;
import com.project.AppraisalSystem.entity.User;
import com.project.AppraisalSystem.exception.BadRequestException;
import com.project.AppraisalSystem.repository.UserRepository;
import com.project.AppraisalSystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    //  LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Invalid email or password");
        }

        if (!user.getIsActive()) {
            throw new BadRequestException("Account is inactive");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserId());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> findUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<List<UserResponseDTO>> findAllUsersByDepartment(@PathVariable Long deptId) {
        return ResponseEntity.ok(userService.findAllUsersByDepartment(deptId));
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<UserResponseDTO>> findAllUsersByManager(@PathVariable Long managerId) {
        return ResponseEntity.ok(userService.findAllUsersByManager(managerId));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(dto));
    }

    @PatchMapping("/{id}/firstname")
    public ResponseEntity<UserResponseDTO> updateFirstName(@PathVariable Long id,
                                                           @RequestParam String firstName) {
        return ResponseEntity.ok(userService.updateFirstName(id, firstName));
    }

    @PatchMapping("/{id}/lastname")
    public ResponseEntity<UserResponseDTO> updateLastName(@PathVariable Long id,
                                                          @RequestParam String lastName) {
        return ResponseEntity.ok(userService.updateLastName(id, lastName));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id,
                                                 @RequestParam String oldPassword,
                                                 @RequestParam String newPassword) {
        return ResponseEntity.ok(userService.updatePassword(id, oldPassword, newPassword));
    }

    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable Long id,
                                                @RequestParam String newPassword) {
        return ResponseEntity.ok(userService.resetPassword(id, newPassword));
    }

    @PatchMapping("/{id}/designation")
    public ResponseEntity<UserResponseDTO> updateDesignation(@PathVariable Long id,
                                                             @RequestParam String designation) {
        return ResponseEntity.ok(userService.updateDesignation(id, designation));
    }

    @PatchMapping("/{id}/manager")
    public ResponseEntity<UserResponseDTO> updateManager(@PathVariable Long id,
                                                         @RequestParam Long managerId) {
        return ResponseEntity.ok(userService.updateManager(id, managerId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id,
                                               @RequestParam Boolean isActive) {
        return ResponseEntity.ok(userService.updateStatus(id, isActive));
    }
    @PatchMapping("/{id}/department")
    public ResponseEntity<UserResponseDTO> updateDepartment(@PathVariable Long id,
                                                            @RequestParam Long deptId) {
        return ResponseEntity.ok(userService.updateDepartment(id, deptId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteById(id));
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<String> deleteByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.deleteByEmail(email));
    }
}
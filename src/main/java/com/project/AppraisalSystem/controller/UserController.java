package com.project.AppraisalSystem.controller;

import com.project.AppraisalSystem.dto.BulkUploadResultDTO;
import com.project.AppraisalSystem.dto.LoginRequestDTO;
import com.project.AppraisalSystem.dto.LoginResponseDTO;
import com.project.AppraisalSystem.dto.UserRequestDTO;
import com.project.AppraisalSystem.dto.UserResponseDTO;
import com.project.AppraisalSystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(userService.login(dto));
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

    @PostMapping("/bulk-upload")
    public ResponseEntity<BulkUploadResultDTO> bulkUploadUsers(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.bulkUploadUsers(file));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteById(id));
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<String> deleteByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.deleteByEmail(email));
    }
}
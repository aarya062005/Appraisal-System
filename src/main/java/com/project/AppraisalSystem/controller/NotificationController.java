package com.project.AppraisalSystem.controller;
import com.project.AppraisalSystem.dto.NotificationRequestDTO;
import com.project.AppraisalSystem.dto.NotificationResponseDTO;
import com.project.AppraisalSystem.dto.NotificationSummaryDTO;
import com.project.AppraisalSystem.entity.enums.NotificationType;
import com.project.AppraisalSystem.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationSummaryDTO>> findAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findAllByUser(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationSummaryDTO>> findUnreadByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findUnreadByUser(userId));
    }

    @GetMapping("/user/{userId}/read")
    public ResponseEntity<List<NotificationSummaryDTO>> findReadByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findReadByUser(userId));
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> findById(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.findById(notificationId));
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countUnreadByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.countUnreadByUser(userId));
    }

    @GetMapping("/user/{userId}/type")
    public ResponseEntity<List<NotificationSummaryDTO>> findByUserAndType(
            @PathVariable Long userId,
            @RequestParam NotificationType type) {
        return ResponseEntity.ok(notificationService.findByUserAndType(userId, type));
    }

    @GetMapping("/user/{userId}/after")
    public ResponseEntity<List<NotificationSummaryDTO>> findByUserAndCreatedAfter(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(notificationService.findByUserAndCreatedAfter(userId, date));
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @RequestBody NotificationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.createNotification(dto));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<String> markAllAsRead(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.markAllAsRead(userId));
    }

    @PatchMapping("/{notificationId}/unread")
    public ResponseEntity<String> markAsUnread(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markAsUnread(notificationId));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.deleteNotification(notificationId));
    }

    @DeleteMapping("/user/{userId}/read")
    public ResponseEntity<String> deleteAllReadByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.deleteAllReadByUser(userId));
    }

    @DeleteMapping("/user/{userId}/all")
    public ResponseEntity<String> deleteAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.deleteAllByUser(userId));
    }
}
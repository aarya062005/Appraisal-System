package com.project.AppraisalSystem.service;

import com.project.AppraisalSystem.entity.Notification;
import com.project.AppraisalSystem.entity.User;
import com.project.AppraisalSystem.entity.enums.NotificationType;
import com.project.AppraisalSystem.entity.enums.Roles;
import com.project.AppraisalSystem.repository.NotificationRepository;
import com.project.AppraisalSystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class NotificationHelper {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // ── Send to one user ──────────────────────────────────────────────────
    public void send(User user, NotificationType type, String title, String message) {
        if (user == null) return;
        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    // ── Send to all HR users ──────────────────────────────────────────────
    public void sendToAllHR(NotificationType type, String title, String message) {
        List<User> hrUsers = userRepository.findAllByRole(Roles.HR);
        hrUsers.forEach(hr -> send(hr, type, title, message));
    }

    // ── Send to a specific user by ID (safe — no-op if not found) ────────
    public void sendToUser(Long userId, NotificationType type, String title, String message) {
        userRepository.findById(userId)
                .ifPresent(user -> send(user, type, title, message));
    }
}
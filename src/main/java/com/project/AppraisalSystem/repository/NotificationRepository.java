package com.project.AppraisalSystem.repository;

import com.project.AppraisalSystem.entity.Notification;
import com.project.AppraisalSystem.entity.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUser_UserId(Long userId);
    List<Notification> findAllByUser_UserIdAndIsRead(Long userId, Boolean isRead);
    List<Notification> findAllByUser_UserIdAndType(Long userId, NotificationType type);
    List<Notification> findAllByUser_UserIdAndCreatedAtAfter(Long userId, LocalDateTime date);
    Long countByUser_UserIdAndIsRead(Long userId, Boolean isRead);
}
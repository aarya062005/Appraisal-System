package com.project.AppraisalSystem.service;

import com.project.AppraisalSystem.dto.NotificationRequestDTO;
import com.project.AppraisalSystem.dto.NotificationResponseDTO;
import com.project.AppraisalSystem.dto.NotificationSummaryDTO;
import com.project.AppraisalSystem.entity.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService  {
    //--------------------------GET--------------------------------------
    List<NotificationSummaryDTO> findAllByUser(Long userId);
    List<NotificationSummaryDTO> findUnreadByUser(Long userId);
    List<NotificationSummaryDTO> findReadByUser(Long userId);
    NotificationResponseDTO findById(Long notificationId);
    Long countUnreadByUser(Long userId);
    List<NotificationSummaryDTO> findByUserAndType(Long userId, NotificationType type);
    List<NotificationSummaryDTO> findByUserAndCreatedAfter(Long userId, LocalDateTime date);

    //------------------------CREATE----------------------------------------

    NotificationResponseDTO createNotification(NotificationRequestDTO dto);

    // ----------------------------UPDATE--------------------------------
    String markAsRead(Long notificationId);
    String markAllAsRead(Long userId);
    String markAsUnread(Long notificationId);

    //-----------------------------------DELETE-----------------------------------------
    String deleteNotification(Long notificationId);
    String deleteAllReadByUser(Long userId);
    String deleteAllByUser(Long userId);
}

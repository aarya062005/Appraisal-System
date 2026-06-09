package com.project.AppraisalSystem.service.implementation;

import com.project.AppraisalSystem.dto.NotificationRequestDTO;
import com.project.AppraisalSystem.dto.NotificationResponseDTO;
import com.project.AppraisalSystem.dto.NotificationSummaryDTO;
import com.project.AppraisalSystem.entity.Notification;
import com.project.AppraisalSystem.entity.User;
import com.project.AppraisalSystem.entity.enums.NotificationType;
import com.project.AppraisalSystem.exception.ResourceNotFoundException;
import com.project.AppraisalSystem.repository.NotificationRepository;
import com.project.AppraisalSystem.repository.UserRepository;
import com.project.AppraisalSystem.service.NotificationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private NotificationResponseDTO toResponseDTO(Notification notification) {
        NotificationResponseDTO dto = modelMapper.map(notification, NotificationResponseDTO.class);
        if (notification.getUser() != null) {
            dto.setUserId(notification.getUser().getUserId());
            dto.setUserEmail(notification.getUser().getEmail());
        }
        return dto;
    }

    private NotificationSummaryDTO toSummaryDTO(Notification notification) {
        return modelMapper.map(notification, NotificationSummaryDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationSummaryDTO> findAllByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return notificationRepository.findAllByUser_UserId(userId)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationSummaryDTO> findUnreadByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return notificationRepository.findAllByUser_UserIdAndIsRead(userId, false)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationSummaryDTO> findReadByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return notificationRepository.findAllByUser_UserIdAndIsRead(userId, true)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponseDTO findById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + notificationId));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countUnreadByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return notificationRepository.countByUser_UserIdAndIsRead(userId, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationSummaryDTO> findByUserAndType(Long userId, NotificationType type) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return notificationRepository.findAllByUser_UserIdAndType(userId, type)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationSummaryDTO> findByUserAndCreatedAfter(Long userId, LocalDateTime date) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return notificationRepository.findAllByUser_UserIdAndCreatedAtAfter(userId, date)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponseDTO createNotification(NotificationRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + dto.getUserId()));
        Notification notification = Notification.builder()
                .user(user)
                .type(dto.getType())
                .title(dto.getTitle())
                .message(dto.getMessage())
                .isRead(false)
                .build();
        return toResponseDTO(notificationRepository.save(notification));
    }

    @Override
    public String markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + notificationId));
        notification.setIsRead(true);
        notificationRepository.save(notification);
        return "Notification marked as read";
    }

    @Override
    public String markAllAsRead(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        List<Notification> unread = notificationRepository
                .findAllByUser_UserIdAndIsRead(userId, false);
        unread.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unread);
        return "All notifications marked as read";
    }

    @Override
    public String markAsUnread(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + notificationId));
        notification.setIsRead(false);
        notificationRepository.save(notification);
        return "Notification marked as unread";
    }

    @Override
    public String deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + notificationId));
        notificationRepository.delete(notification);
        return "Notification deleted successfully";
    }

    @Override
    public String deleteAllReadByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        List<Notification> read = notificationRepository
                .findAllByUser_UserIdAndIsRead(userId, true);
        notificationRepository.deleteAll(read);
        return "All read notifications cleared";
    }

    @Override
    public String deleteAllByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        List<Notification> all = notificationRepository
                .findAllByUser_UserId(userId);
        notificationRepository.deleteAll(all);
        return "All notifications cleared";
    }
}
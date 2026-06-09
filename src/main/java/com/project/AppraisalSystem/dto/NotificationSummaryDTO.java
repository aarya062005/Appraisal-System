
package com.project.AppraisalSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.AppraisalSystem.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationSummaryDTO {
    private Long notificationId;
    private String title;
    private NotificationType type;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
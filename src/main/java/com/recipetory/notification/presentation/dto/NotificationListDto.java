package com.recipetory.notification.presentation.dto;

import com.recipetory.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class NotificationListDto {
    private List<NotificationDto> notifications;

    public static NotificationListDto fromEntityList(
            List<Notification> notifications
    ) {
        return new NotificationListDto(
                notifications.stream()
                        .map(NotificationDto::fromEntity).toList());
    }
}

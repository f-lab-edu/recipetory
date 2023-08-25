package com.recipetory.notification.domain;

import com.recipetory.notification.presentation.dto.NotificationMessage;

public interface NotificationMessageSender {
    void sendNotificationMessage(NotificationMessage message);
}

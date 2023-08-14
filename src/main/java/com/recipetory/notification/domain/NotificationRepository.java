package com.recipetory.notification.domain;

import com.recipetory.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);

    Optional<Notification> findById(Long id);

    List<Notification> findByReceiver(User receiver);

    void deleteById(Long notificationId);

    void deleteAllByReceiver(User receiver);
}

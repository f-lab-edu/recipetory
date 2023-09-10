package com.recipetory.notification.application;

import com.recipetory.notification.domain.Notification;
import com.recipetory.notification.domain.NotificationRepository;
import com.recipetory.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TestNotificationRepository implements NotificationRepository {
    private final List<Notification> notifications = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong(1L);

    @Override
    public Notification save(Notification notification) {
        Notification created = Notification.builder()
                .id(atomicLong.getAndIncrement())
                .notificationType(notification.getNotificationType())
                .message(notification.getMessage())
                .sender(notification.getSender()).receiver(notification.getReceiver())
                .path(notification.getPath()).build();
        notifications.add(created);
        return created;
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notifications.stream().filter(notification ->
                notification.getId().equals(id)).findAny();
    }

    @Override
    public List<Notification> findByReceiver(User receiver) {
        return notifications.stream().filter(notification ->
                notification.getReceiver().getId().equals(receiver.getId())).toList();
    }

    @Override
    public void deleteById(Long notificationId) {
        findById(notificationId).ifPresent(notifications::remove);
    }

    @Override
    public void deleteAllByReceiver(User receiver) {
        findByReceiver(receiver).forEach(notifications::remove);
    }
}

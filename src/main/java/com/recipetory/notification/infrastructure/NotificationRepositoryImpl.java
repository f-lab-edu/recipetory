package com.recipetory.notification.infrastructure;

import com.recipetory.notification.domain.Notification;
import com.recipetory.notification.domain.NotificationRepository;
import com.recipetory.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id);
    }

    @Override
    public List<Notification> findByReceiver(User receiver) {
        return notificationJpaRepository.findByReceiver(receiver);
    }

    @Override
    public void deleteById(Long notificationId) {
        notificationJpaRepository.deleteById(notificationId);
    }

    @Override
    public void deleteAllByReceiver(User receiver) {
        notificationJpaRepository.deleteAllByReceiver(receiver);
    }
}

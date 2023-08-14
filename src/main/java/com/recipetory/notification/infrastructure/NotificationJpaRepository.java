package com.recipetory.notification.infrastructure;

import com.recipetory.notification.domain.Notification;
import com.recipetory.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver(User receiver);

    void deleteAllByReceiver(User receiver);
}

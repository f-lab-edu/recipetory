package com.recipetory.notification.infrastructure;

import com.recipetory.notification.application.TestNotificationRepository;
import com.recipetory.notification.domain.Notification;
import com.recipetory.notification.domain.NotificationRepository;
import com.recipetory.notification.domain.NotificationType;
import com.recipetory.notification.presentation.dto.NotificationMessage;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.follow.FollowRepository;
import com.recipetory.user.service.TestFollowRepository;
import com.recipetory.user.service.TestUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationListenerTest {
    private KafkaNotificationListener notificationListener;
    NotificationRepository notificationRepository = new TestNotificationRepository();
    UserRepository userRepository = new TestUserRepository();
    FollowRepository followRepository = new TestFollowRepository();

    @BeforeEach
    public void setUp() {
        notificationListener = new KafkaNotificationListener(
                notificationRepository, userRepository, followRepository);
    }

    @Test
    @DisplayName("도착한 알림 메세지에 맞게 알림을 저장한다.")
    public void notificationListenerTest() {
        // given : sender, receiver 유저가 설정된 NotificationMessage가 발행된다.
        User sender = userRepository.save(
                User.builder().name("sender").email("sender@email.com").role(Role.USER).build());
        User receiver = userRepository.save(
                User.builder().name("receiver").email("receiver@email.com").role(Role.USER).build());
        NotificationMessage message = NotificationMessage.builder()
                .senderId(sender.getId()).receiverId(receiver.getId())
                .notificationType(NotificationType.FOLLOW).build();

        // when :
        notificationListener.saveNotification(message);

        // then : 메세지로 전송된 것과 같은 알림 entity가 저장되었다.
        List<Notification> found = notificationRepository.findByReceiver(receiver);
        assertAll(() -> assertEquals(1,found.size()),
                () -> assertEquals(sender.getId(), found.get(0).getSender().getId()),
                () -> assertEquals(message.getNotificationType(), found.get(0).getNotificationType()));
    }
}

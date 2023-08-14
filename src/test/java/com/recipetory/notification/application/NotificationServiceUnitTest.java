package com.recipetory.notification.application;

import com.recipetory.notification.domain.Notification;
import com.recipetory.notification.domain.NotificationRepository;
import com.recipetory.notification.domain.NotificationType;
import com.recipetory.notification.presentation.dto.NotificationDto;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.exception.NotOwnerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceUnitTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationService notificationService;

    User receiver;
    Notification notification1, notification2;
    List<Notification> notifications = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        receiver = User.builder().id(1L).name("receiver").email("receiver@test.com").role(Role.USER).build();

        notification1 = Notification.builder()
                .id(1L).notificationType(NotificationType.NEW_RECIPE)
                .receiver(receiver).build();
        notification2 = Notification.builder()
                .id(2L).notificationType(NotificationType.ETC)
                .receiver(receiver).build();
        notifications.addAll(Arrays.asList(notification1, notification2));
    }

    @Test
    @DisplayName("유저의 이메일로 알림이 조회된다.")
    public void testGetNotification() {
        // given : receiver 유저가 receiver인 알림 2개가 생성된다.(setUp)
        when(userService.getUserByEmail(receiver.getEmail())).thenReturn(receiver);
        when(notificationRepository.findByReceiver(receiver)).thenReturn(notifications);

        // when : receiver email로 조회한다.
        List<NotificationDto> notificationDtos =
                notificationService.getNotificationsByReceiverEmail(
                        receiver.getEmail()).getNotifications();

        // then : 조회된 알림과 생성된 알림의 개수가 같다.
        assertEquals(2, notificationDtos.size());

    }

    @Test
    @DisplayName("notification id로 알림 읽음 처리가 가능하다.")
    public void checkNotificationTest() {
        // given : receiver가 receiver, notification id가 1L인 알림이 생성된다.
        Long notificationId = 1L;
        when(userService.getUserByEmail(receiver.getEmail())).thenReturn(receiver);
        when(notificationRepository.findById(notification1.getId())).thenReturn(Optional.ofNullable(notification1));

        // when : id를 통해 알림을 check하는 메소드를 수행한다.
        notificationService.checkNotification(notificationId, receiver.getEmail());

        // then : 알람의 읽음 상태(isRead)는 true이다.
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    assertTrue(notification.isRead());
                });
    }

    @Test
    @DisplayName("특정 유저의 모든 알림을 읽음 처리하는 것이 가능하다.")
    public void checkAllTest() {
        // given : receiver 유저가 receiver인 알림 2개가 생성된다.(setUp)
        when(userService.getUserByEmail(receiver.getEmail())).thenReturn(receiver);
        when(notificationRepository.findByReceiver(receiver)).thenReturn(notifications);

        // when : receiver 유저의 알림 전체를 읽음처리 한다.
        notificationService.checkAllNotificationByEmail(receiver.getEmail());

        // then : 알림 전체의 읽음상태(isRead)는 true이다.
        notificationRepository.findByReceiver(receiver).forEach(notification -> {
            assertTrue(notification.isRead());
        });
    }

    @Test
    @DisplayName("알림의 주인이 아닌 유저가 알림을 삭제하려고 하면 예외가 발생한다.")
    public void notificationOwnerTest() {
        // given : receiver가 receiver, notification id가 1L인 알림이 생성된다.
        Long notificationId = 1L;
        when(notificationRepository.findById(notification1.getId())).thenReturn(Optional.ofNullable(notification1));

        // given2 : 알람의 주인이 아닌 another 유저가 존재한다.
        User another = User.builder().id(2L).name("another").email("another@test.com").role(Role.USER).build();
        when(userService.getUserByEmail(another.getEmail())).thenReturn(another);

        // when, then : another 유저가 알람을 삭제하려고 하면, NotOwnerException이 발생한다.
        assertThrows(NotOwnerException.class,() ->
                notificationService.deleteNotification(notificationId,another.getEmail()));
    }
}
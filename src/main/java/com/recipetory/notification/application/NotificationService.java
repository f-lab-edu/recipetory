package com.recipetory.notification.application;

import com.recipetory.notification.domain.Notification;
import com.recipetory.notification.domain.NotificationRepository;
import com.recipetory.notification.presentation.dto.NotificationListDto;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.exception.NotOwnerException;
import com.recipetory.utils.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    /**
     * 현재 로그인한 유저의 알림을 조회한다.
     * @param logInEmail
     * @return
     */
    @Transactional(readOnly = true)
    public NotificationListDto getNotificationsByReceiverEmail(String logInEmail) {
        User user = userService.getUserByEmail(logInEmail);
        List<Notification> notifications = notificationRepository.findByReceiver(user);

        return NotificationListDto.fromEntityList(notifications);
    }

    /**
     * id로 조회된 알림을 읽음처리한다.
     * @param notificationId
     * @param logInEmail
     */
    @Transactional
    public void checkNotification(Long notificationId, String logInEmail) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Notification", String.valueOf(notificationId)));
        User user = userService.getUserByEmail(logInEmail);

        validateNotificationOwner(notification, user);

        notification.checkNotification();
    }

    /**
     * 특정 email을 가진 유저의 알림 전체를 읽음처리한다.
     * @param logInEmail
     */
    @Transactional
    public void checkAllNotificationByEmail(String logInEmail) {
        User user = userService.getUserByEmail(logInEmail);

        notificationRepository.findByReceiver(user)
                .forEach(Notification::checkNotification);
    }

    /**
     * id로 조회된 알림을 읽음처리한다.
     * @param notificationId
     * @param logInEmail
     */
    @Transactional
    public void deleteNotification(Long notificationId, String logInEmail) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Notification", String.valueOf(notificationId)));
        User user = userService.getUserByEmail(logInEmail);

        validateNotificationOwner(notification, user);

        notificationRepository.deleteById(notificationId);
    }

    /**
     * 특정 email을 가진 유저의 알림 전체를 삭제한다.
     * @param logInEmail
     */
    @Transactional
    public void deleteAllNotificationByEmail(String logInEmail) {
        User user = userService.getUserByEmail(logInEmail);

        notificationRepository.deleteAllByReceiver(user);
    }

    /**
     * User가 Notifacation의 주인인지 확인한다.
     * @param notification
     * @param receiver
     */
    @Transactional(readOnly = true)
    public void validateNotificationOwner(
            Notification notification, User receiver) {

        if (notification.getReceiver() != receiver) {
            throw new NotOwnerException(receiver.getId(),
                    notification.getReceiver().getId(),
                    "Notification",
                    notification.getId().toString());
        }
    }
}

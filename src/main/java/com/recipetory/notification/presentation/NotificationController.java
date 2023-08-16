package com.recipetory.notification.presentation;

import com.recipetory.config.auth.argumentresolver.LogInUser;
import com.recipetory.config.auth.dto.SessionUser;
import com.recipetory.notification.application.NotificationService;
import com.recipetory.notification.presentation.dto.NotificationListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * 현재 로그인한 유저의 알림 목록을 조회한다.
     * @param logInUser
     * @return 알림 목록
     */
    @GetMapping("/notifications")
    public ResponseEntity<NotificationListDto> getNotifications(
            @LogInUser SessionUser logInUser) {
        NotificationListDto notificationListDto =
                notificationService.getNotificationsByReceiverEmail(logInUser.getEmail());

        return ResponseEntity.ok(notificationListDto);
    }

    /**
     * notificationId에 맞는 알림을 읽음처리한다.
     * @param notificationId
     * @param logInUser
     * @return
     */
    @PutMapping("/notifications/{notificationId}")
    public ResponseEntity<Void> checkNotification(
            @PathVariable Long notificationId,
            @LogInUser SessionUser logInUser) {
        notificationService.checkNotification(notificationId, logInUser.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * 현재 로그인한 유저의 알림을 모두 읽음처리한다.
     * @param logInUser
     * @return
     */
    @PutMapping("/notifications")
    public ResponseEntity<Void> checkAllNotification(
            @LogInUser SessionUser logInUser) {
        notificationService.checkAllNotificationByEmail(logInUser.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * notificationId에 맞는 알림을 삭제한다.
     * @param notificationId
     * @return
     */
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId,
            @LogInUser SessionUser logInUser) {
        notificationService.deleteNotification(notificationId, logInUser.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * 현재 로그인한 유저의 알림 전체를 삭제한다.
     * @param logInUser
     * @return
     */
    @DeleteMapping("/notifications")
    public ResponseEntity<Void> deleteAllNotification(
            @LogInUser SessionUser logInUser) {
        notificationService.deleteAllNotificationByEmail(logInUser.getEmail());
        return ResponseEntity.ok().build();
    }
}

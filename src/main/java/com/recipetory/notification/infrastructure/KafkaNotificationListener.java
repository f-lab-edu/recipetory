package com.recipetory.notification.infrastructure;

import com.recipetory.config.kafka.KafkaTopic;
import com.recipetory.notification.domain.Notification;
import com.recipetory.notification.domain.NotificationRepository;
import com.recipetory.notification.domain.NotificationType;
import com.recipetory.notification.presentation.dto.NotificationMessage;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.follow.FollowRepository;
import com.recipetory.utils.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaNotificationListener {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    /**
     * 도착한 message 정보를 토대로 notification save를 진행한다.
     * @param notificationMessage sent kafka message
     */
    @KafkaListener(topics = KafkaTopic.NOTIFICATION,
            containerFactory = "notificationKafkaListenerContainerFactory",
            groupId = "${spring.kafka.group-id}")
    @Transactional
    public void notificationListener(NotificationMessage notificationMessage) {
        NotificationType type = notificationMessage.getNotificationType();

        if (type == NotificationType.NEW_RECIPE) {
            // 팔로워들 전체에게 알림
            saveNotificationToFollowers(notificationMessage);
        } else {
            saveNotification(notificationMessage);
        }
    }

    @Transactional
    public void saveNotificationToFollowers(NotificationMessage notificationMessage) {
        User sender = getUserById(notificationMessage.getSenderId());
        NotificationType type = notificationMessage.getNotificationType();

        followRepository.findByFollowed(sender).forEach(follow -> {
            User receiver = follow.getFollowing();

            Notification notification = Notification.builder()
                    .sender(sender).receiver(receiver)
                    .message(type.getDefaultMessage(sender))
                    .path(notificationMessage.getPath())
                    .notificationType(type).build();

            notificationRepository.save(notification);
        });
    }

    @Transactional
    public void saveNotification(NotificationMessage notificationMessage) {
        User sender = getUserById(notificationMessage.getSenderId());
        User receiver = getUserById(notificationMessage.getReceiverId());
        NotificationType type = notificationMessage.getNotificationType();

        Notification notification = Notification.builder()
                .sender(sender).receiver(receiver)
                .message(type.getDefaultMessage(sender))
                .path(notificationMessage.getPath())
                .notificationType(type).build();

        notificationRepository.save(notification);
    }

    @Transactional
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User", String.valueOf(userId)));
    }
}

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
     * notification sender와 receiever가 정해져있다.
     * @param notificationMessage sent kafka message
     */
    @KafkaListener(topics = KafkaTopic.NOTIFICATION,
            containerFactory = "notificationKafkaListenerContainerFactory",
            groupId = "${spring.kafka.group-id}")
    @Transactional
    public void saveNotification(NotificationMessage notificationMessage) {
        log.info("notification message consume : {}",notificationMessage.getPath());
        NotificationType type = notificationMessage.getNotificationType();
        User sender = getUserById(notificationMessage.getSenderId());
        User receiver = getUserById(notificationMessage.getReceiverId());

        Notification notification = Notification.builder()
                .sender(sender).receiver(receiver).notificationType(type)
                .path(notificationMessage.getPath())
                .message(type.getDefaultMessage(sender)).build();

        notificationRepository.save(notification);
    }

    /**
     * 도착한 message 정보를 토대로 notification save를 진행한다.
     * notification sender의 팔로워들에게 알림을 일괄적으로 보낸다.
     * @param notificationMessage sent kafka message
     */
    @KafkaListener(topics = KafkaTopic.FOLLOWER_NOTIFICATION,
            containerFactory = "notificationKafkaListenerContainerFactory",
            groupId = "${spring.kafka.group-id}")
    @Transactional
    public void saveFollowerNotification(NotificationMessage notificationMessage) {
        log.info("follower-notification message consume : {}",notificationMessage.getPath());
        NotificationType type = notificationMessage.getNotificationType();
        User sender = getUserById(notificationMessage.getSenderId());

        followRepository.findByFollowed(sender).forEach(follow -> {
            User follower = follow.getFollowing();

            Notification notification = Notification.builder()
                    .sender(sender).receiver(follower).notificationType(type)
                    .path(notificationMessage.getPath())
                    .message(type.getDefaultMessage(sender)).build();

            notificationRepository.save(notification);
        });
    }

    @Transactional
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User", String.valueOf(userId)));
    }
}

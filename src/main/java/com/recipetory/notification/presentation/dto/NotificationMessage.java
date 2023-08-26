package com.recipetory.notification.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recipetory.config.kafka.KafkaTopic;
import com.recipetory.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * kafka로 전송될 Serializable messsage dto
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private Long senderId;
    private Long receiverId;
    private NotificationType notificationType;
    private String path;

    // 메세지가 전송될 kafka topic
    @JsonIgnore
    private String topic;
}

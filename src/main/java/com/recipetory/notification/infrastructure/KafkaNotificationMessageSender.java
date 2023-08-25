package com.recipetory.notification.infrastructure;

import com.recipetory.config.kafka.KafkaTopic;
import com.recipetory.notification.domain.NotificationMessageSender;
import com.recipetory.notification.presentation.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationMessageSender implements NotificationMessageSender {
    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    @Override
    public void sendNotificationMessage(NotificationMessage message) {
        CompletableFuture<SendResult<String, NotificationMessage>> future =
                kafkaTemplate.send(KafkaTopic.NOTIFICATION, message);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.warn("message sent failed!", ex);
            }
        });
    }
}

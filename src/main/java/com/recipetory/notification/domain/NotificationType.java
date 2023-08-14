package com.recipetory.notification.domain;

import com.recipetory.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    NEW_RECIPE((sender) -> sender + "님이 새 레시피를 작성하셨습니다."),
    FOLLOW((sender) -> sender + "님이 나를 팔로우합니다."),
    COMMENT((sender) -> sender + "님이 나의 레시피에 댓글을 달았습니다."),
    REVIEW((sender) -> sender + "님이 나의 레시피에 리뷰를 달았습니다."),
    ETC((sender) -> sender + "님으로부터의 알림");

    private final Function<String, String> defaultMessage;

    public String getDefaultMessage(User sender) {
        return defaultMessage.apply(sender.getName());
    }
}

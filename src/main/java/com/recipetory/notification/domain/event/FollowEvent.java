package com.recipetory.notification.domain.event;

import com.recipetory.user.domain.follow.Follow;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowEvent {
    private final Follow follow;
}

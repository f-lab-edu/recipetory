package com.recipetory.user.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CannotFollowException extends RuntimeException {
    private Long followingId;
    private Long followedId;
}

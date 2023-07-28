package com.recipetory.user.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserNotFoundException extends RuntimeException {
    private String key;
}

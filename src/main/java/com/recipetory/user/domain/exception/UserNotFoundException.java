package com.recipetory.user.domain.exception;

import com.recipetory.user.domain.UserKeyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserNotFoundException extends RuntimeException {
    private UserKeyType userKeyType;
    private String key;
}

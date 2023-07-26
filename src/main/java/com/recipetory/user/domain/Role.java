package com.recipetory.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Role enum type for security
 * */
@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    GUEST("ROLE_GUEST");

    private final String key;
}

package com.recipetory.user.domain.exception;

import com.recipetory.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidUserRoleException extends RuntimeException {
    private Long userId;
    private Role currentRole;
    private Role requiredRole;
}

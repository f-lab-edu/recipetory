package com.recipetory.user.domain.exception;

import com.recipetory.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidUserRoleException extends RuntimeException {
    private Role requiredRole;
}

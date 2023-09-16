package com.recipetory.config.auth.dto;

import com.recipetory.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionUser {
    private String name;
    private String email;
    private Long id;

    public static SessionUser fromEntity(User user) {
        return SessionUser.builder()
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId())
                .build();
    }
}

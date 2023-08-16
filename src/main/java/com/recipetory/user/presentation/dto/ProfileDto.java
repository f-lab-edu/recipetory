package com.recipetory.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.recipetory.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private String provider;
    private String profileImage = "";
    private int recipeNumber = 0;

    public static ProfileDto fromEntity(User user) {
        return ProfileDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .provider(user.getProvider())
                .profileImage(user.getImageUrl())
                .recipeNumber(user.getRecipes().size())
                .build();
    }
}

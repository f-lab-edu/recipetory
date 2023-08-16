package com.recipetory.user.presentation.dto;

import com.recipetory.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 간단한 유저 정보에 대한 리스트가 필요할 때 사용
 * 팔로우 리스트 등
 * */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserListDto {
    List<UserDto> users;

    public static UserListDto fromEntityList(List<User> follows) {
        return new UserListDto(follows.stream()
                .map(UserDto::fromEntity).toList());
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    static class UserDto {
        private Long id;
        private String name;
        private String bio;

        private static UserDto fromEntity(User user) {
            return UserDto.builder()
                    .id(user.getId()).name(user.getName()).bio(user.getBio())
                    .build();
        }
    }
}

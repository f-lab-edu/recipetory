package com.recipetory.user.presentation.dto;

import com.recipetory.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 팔로우하거나 팔로잉하는 유저 simple list
 * */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FollowDto {
    List<SimpleUserDto> followList;

    public static FollowDto fromEntityList(List<User> follows) {
        return new FollowDto(follows.stream()
                .map(SimpleUserDto::fromEntity).toList());
    }
}

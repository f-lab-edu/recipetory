package com.recipetory.user.presentation.dto;

import com.recipetory.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저의 간단한 정보 (id, 닉네임)
 * 간단한 유저 목록에 대한 리스트가 필요할 때 내용으로 들어갈 수 있습니다
 * */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SimpleUserDto {
    private Long id;
    private String name;

    public static SimpleUserDto fromEntity(User user) {
        return SimpleUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}

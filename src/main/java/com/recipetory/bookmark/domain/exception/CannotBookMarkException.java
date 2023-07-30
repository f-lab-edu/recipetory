package com.recipetory.bookmark.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CannotBookMarkException extends RuntimeException {
    private Long userId;
    private Long recipeId;

    @Override
    public String getMessage() {
        return "[recipeId: "+ recipeId + "]의 주인 [userId: " +
                userId + "]는 북마크 할 수 없습니다.";
    }
}

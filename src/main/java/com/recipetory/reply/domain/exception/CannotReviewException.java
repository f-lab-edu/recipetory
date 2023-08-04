package com.recipetory.reply.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CannotReviewException extends RuntimeException {
    private Long authorId;
    private Long recipeId;
}

package com.recipetory.recipe.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecipeNotFoundException extends RuntimeException {
    private String key;
}

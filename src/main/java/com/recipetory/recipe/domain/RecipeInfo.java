package com.recipetory.recipe.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeInfo {
    private String description = "";

    // 각 ENUM의 default value
    @Enumerated(EnumType.STRING)
    private CookingTime cookingTime = CookingTime.UNDEFINED;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty = Difficulty.UNDEFINED;

    @Enumerated(EnumType.STRING)
    private Serving serving = Serving.UNDEFINED;
}

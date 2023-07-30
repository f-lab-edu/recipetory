package com.recipetory.recipe.domain;

import jakarta.persistence.Column;
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
    @Column(length = 1000)
    private String description = "";

    // 각 ENUM의 default value
    @Column
    @Enumerated(EnumType.STRING)
    private CookingTime cookingTime = CookingTime.UNDEFINED;

    @Column
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty = Difficulty.UNDEFINED;

    @Column
    @Enumerated(EnumType.STRING)
    private Serving serving = Serving.UNDEFINED;
}

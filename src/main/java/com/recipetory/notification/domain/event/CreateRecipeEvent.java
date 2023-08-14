package com.recipetory.notification.domain.event;

import com.recipetory.recipe.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRecipeEvent {
    private final Recipe recipe;
}

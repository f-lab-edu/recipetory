package com.recipetory.notification.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteRecipeEvent {
    private Long recipeId;
}

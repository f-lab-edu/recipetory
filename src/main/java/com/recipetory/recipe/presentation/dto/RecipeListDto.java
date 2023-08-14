package com.recipetory.recipe.presentation.dto;

import com.recipetory.recipe.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeListDto {
    private List<RecipeTitleDto> recipes = new ArrayList<>();

    public static RecipeListDto fromEntityList(List<Recipe> recipes) {
        List<RecipeTitleDto> recipeDtos = recipes.stream()
                .map(RecipeTitleDto::fromEntity).toList();

        return new RecipeListDto(recipeDtos);
    }
}

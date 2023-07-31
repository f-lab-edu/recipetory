package com.recipetory.recipe.presentation.dto;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeInfo;
import com.recipetory.recipe.domain.RecipeStatistics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RecipeTitleDto {
    private Long id;
    private String title;
    private String description;
    private int viewCount;
    private String ratings;

    public static RecipeTitleDto fromEntity(Recipe recipe) {
        RecipeInfo recipeInfo = recipe.getRecipeInfo();
        RecipeStatistics recipeStatistics = recipe.getRecipeStatistics();

        return RecipeTitleDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipeInfo.getDescription())
                .viewCount(recipeStatistics.getViewCount())
                .ratings(recipeStatistics.getRatingFormat())
                .build();
    }
}

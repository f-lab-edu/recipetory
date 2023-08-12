package com.recipetory.recipe.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeInfo;
import com.recipetory.recipe.domain.RecipeStatistics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 레시피의 개략적인 내용이 보이는 레시피 리스트에서 사용할 수 있습니다
 * */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
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

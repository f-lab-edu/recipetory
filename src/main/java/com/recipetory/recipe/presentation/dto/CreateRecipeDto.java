package com.recipetory.recipe.presentation.dto;

import com.recipetory.ingredient.domain.Ingredient;
import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.ingredient.presentation.dto.CreateIngredientDto;
import com.recipetory.ingredient.presentation.dto.IngredientDto;
import com.recipetory.recipe.domain.*;
import com.recipetory.step.domain.Step;
import com.recipetory.step.presentation.dto.CreateStepDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeDto {
    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotNull
        @NotBlank
        private String title;

        // RecipeInfo
        private String description = "";
        private CookingTime cookingTime = CookingTime.UNDEFINED;
        private Difficulty difficulty = Difficulty.UNDEFINED;
        private Serving serving = Serving.UNDEFINED;

        // steps & ingredients (relation)
        private List<CreateStepDto.Request> steps
                = new ArrayList<>();
        private List<IngredientDto> ingredients
                = new ArrayList<>();

        // TODO : Tag


        // recipe entity without m:n relation
        public Recipe toEntity() {
            RecipeInfo recipeInfo = RecipeInfo.builder()
                    .description(description)
                    .cookingTime(cookingTime)
                    .difficulty(difficulty)
                    .serving(serving)
                    .build();

            // '일대다'로 종속된 steps
            List<Step> steps = this.steps.stream()
                    .map(CreateStepDto.Request::toEntity)
                    .toList();

            return Recipe.builder()
                    .title(title)
                    .recipeInfo(recipeInfo)
                    .recipeStatistics(new RecipeStatistics()) // statistics : 항상 초기값
                    .steps(steps)
                    .build();
        }

        public List<Ingredient> toIngredientEntities() {
            return ingredients.stream()
                    .map(IngredientDto::toEntity).toList();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        @NotNull
        @NotBlank
        private String title;

        private RecipeInfo recipeInfo;

        private List<CreateStepDto.Response> steps;
        private List<IngredientDto> ingredients;

        public static CreateRecipeDto.Response fromEntity(Recipe recipe) {
            RecipeInfo recipeInfo = recipe.getRecipeInfo();

            List<CreateStepDto.Response> steps = recipe.getSteps().stream()
                    .map(CreateStepDto.Response::fromEntity)
                    .toList();
            List<IngredientDto> ingredients = recipe.getIngredients().stream()
                    .map(IngredientDto::fromEntity)
                    .toList();

            return Response.builder()
                    .title(recipe.getTitle())
                    .recipeInfo(recipeInfo)
                    .steps(steps)
                    .ingredients(ingredients)
                    .build();
        }
    }
}

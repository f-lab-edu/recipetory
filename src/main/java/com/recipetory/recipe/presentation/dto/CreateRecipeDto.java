package com.recipetory.recipe.presentation.dto;

import com.recipetory.ingredient.presentation.dto.RecipeIngredientDto;
import com.recipetory.recipe.domain.*;
import com.recipetory.step.domain.Step;
import com.recipetory.step.presentation.dto.CreateStepDto;
import com.recipetory.tag.domain.Tag;
import com.recipetory.tag.presentation.dto.TagDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeDto {
    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotNull(message = "레시피 제목을 입력해야합니다.")
        @NotBlank(message = "레시피 제목은 공란이어선 안됩니다.")
        private String title;

        // RecipeInfo
        @Length(max = 1000)
        private String description = "";

        private CookingTime cookingTime = CookingTime.UNDEFINED;
        private Difficulty difficulty = Difficulty.UNDEFINED;
        private Serving serving = Serving.UNDEFINED;

        // steps & ingredients (relation)
        private List<CreateStepDto.Request> steps
                = new ArrayList<>();
        private List<RecipeIngredientDto> ingredients
                = new ArrayList<>();

        private List<TagDto> tags = new ArrayList<>();

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

            // '일대다'로 종속된 tags
            List<Tag> tags = this.tags.stream()
                    .map(TagDto::toEntity)
                    .toList();

            return Recipe.builder()
                    .title(title)
                    .recipeInfo(recipeInfo)
                    .recipeStatistics(new RecipeStatistics()) // statistics : 항상 초기값
                    .tags(tags)
                    .steps(steps)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;

        @NotNull
        @NotBlank
        private String title;

        private RecipeInfo recipeInfo;
        private RecipeStatistics recipeStatistics;

        private List<CreateStepDto.Response> steps;
        private List<RecipeIngredientDto> ingredients;
        private List<TagDto> tags;

        public static CreateRecipeDto.Response fromEntity(Recipe recipe) {
            RecipeInfo recipeInfo = recipe.getRecipeInfo();

            List<CreateStepDto.Response> steps = recipe.getSteps().stream()
                    .map(CreateStepDto.Response::fromEntity)
                    .toList();
            List<RecipeIngredientDto> ingredients = recipe.getIngredients().stream()
                    .map(RecipeIngredientDto::fromEntity)
                    .toList();
            List<TagDto> tags = recipe.getTags().stream()
                    .map(TagDto::fromEntity).toList();

            return Response.builder()
                    .id(recipe.getId())
                    .title(recipe.getTitle())
                    .recipeInfo(recipeInfo)
                    .steps(steps)
                    .ingredients(ingredients)
                    .tags(tags)
                    .build();
        }
    }
}

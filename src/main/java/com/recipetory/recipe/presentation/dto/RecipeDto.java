package com.recipetory.recipe.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.ingredient.presentation.dto.RecipeIngredientDto;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeInfo;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.step.domain.Step;
import com.recipetory.step.presentation.dto.StepDto;
import com.recipetory.tag.domain.Tag;
import com.recipetory.tag.domain.TagName;
import com.recipetory.tag.presentation.dto.TagDto;
import com.recipetory.user.domain.User;
import com.recipetory.user.presentation.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDto {
    private Long id;
    private String title;
    private RecipeInfo information;
    private RecipeStatistics statistics;

    // relations
    private UserDto author;
    private List<StepDto> steps;
    private List<RecipeIngredientDto> ingredients;
    private List<TagName> tags;

    /**
     * entity로부터 기본적인 레시피 정보를 dto로 변환한다.
     * @param recipe
     * @return
     */
    public static RecipeDto fromEntity(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .information(recipe.getRecipeInfo())
                .statistics(recipe.getRecipeStatistics())
                .build();
    }

    /**
     * complete recipe information이 필요할 때,
     * 연관 관계 setting;
     * @param author
     * @param steps
     * @param tags
     */
    public void setRelations(
            User author, List<Step> steps, List<RecipeIngredient> ingredients, List<Tag> tags) {
        this.author = UserDto.fromEntity(author);
        this.steps = steps.stream().map(StepDto::fromEntity).toList();
        this.ingredients = ingredients.stream()
                .map(RecipeIngredientDto::fromEntity).toList();
        this.tags = tags.stream()
                .map(Tag::getTagName).toList();
    }

    public static RecipeDto fromDocument(RecipeDocument recipe) {
        return RecipeDto.builder()
                .id(recipe.getId()).title(recipe.getTitle())
                .information(recipe.getRecipeInfo())
                .statistics(recipe.getRecipeStatistics())
                .steps(recipe.getSteps().stream().map(StepDto::fromDocument).toList())
                .ingredients(recipe.getIngredients().stream().map(RecipeIngredientDto::fromDocument).toList())
                .tags(recipe.getTags()).build();
    }
}

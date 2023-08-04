package com.recipetory.recipe.presentation.dto;

import com.recipetory.recipe.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 레시피에 대한 매우 간단한 정보(제목, id)
 * 간단한 레시피 정보나 id가 필요할 때 사용됩니다
 * */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SimpleRecipeDto {
    private Long id;
    private String title;

    public static SimpleRecipeDto fromEntity(Recipe recipe) {
        return SimpleRecipeDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .build();
    }
}

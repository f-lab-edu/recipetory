package com.recipetory.ingredient.presentation.dto;

import com.recipetory.ingredient.domain.Ingredient;
import com.recipetory.ingredient.domain.RecipeIngredient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * RecipeIngredient relation 에 사용되는 DTO
 */
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class RecipeIngredientDto {
    @NotNull(message = "재료 이름이 필요합니다.")
    @NotBlank(message = "재료 이름이 공란이어선 안됩니다.")
    private String name = "";

    @Length(max = 10)
    private String amount = "";

    public static RecipeIngredientDto fromEntity(
            RecipeIngredient recipeIngredient) {
        return RecipeIngredientDto.builder()
                .name(recipeIngredient.getIngredient().getName())
                .amount(recipeIngredient.getAmount())
                .build();
    }
}

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
public class IngredientDto {

    @NotNull
    @NotBlank
    private String name = "";

    @Length(max = 10)
    private String amount = "";

    public Ingredient toEntity() {
        return Ingredient.builder()
                .name(name)
                .build();
    }

    public static IngredientDto fromEntity(
            RecipeIngredient recipeIngredient) {
        return IngredientDto.builder()
                .name(recipeIngredient.getIngredient().getName())
                .amount(recipeIngredient.getAmount())
                .build();
    }
}

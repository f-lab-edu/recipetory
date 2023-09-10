package com.recipetory.recipe.domain.document;

import com.recipetory.ingredient.domain.RecipeIngredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * recipe document의 nested field
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IngredientDocument {
    private String name;
    private String amount;

    private static IngredientDocument fromEntity(RecipeIngredient recipeIngredient) {
        return IngredientDocument.builder()
                .name(recipeIngredient.getIngredient().getName())
                .amount(recipeIngredient.getAmount())
                .build();
    }

    public static List<IngredientDocument> fromEntityList(
            List<RecipeIngredient> recipeIngredients) {
        return recipeIngredients.stream()
                .map(IngredientDocument::fromEntity).toList();
    }
}

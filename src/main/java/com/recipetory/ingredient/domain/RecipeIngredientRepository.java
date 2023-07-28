package com.recipetory.ingredient.domain;

import com.recipetory.recipe.domain.Recipe;

import java.util.List;

public interface RecipeIngredientRepository {
    RecipeIngredient save(RecipeIngredient recipeIngredient);

    List<RecipeIngredient> findByRecipe(Recipe recipe);
}

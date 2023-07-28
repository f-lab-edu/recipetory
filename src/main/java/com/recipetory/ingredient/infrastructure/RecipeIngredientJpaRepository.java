package com.recipetory.ingredient.infrastructure;

import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeIngredientJpaRepository extends JpaRepository<RecipeIngredient,Long> {
    List<RecipeIngredient> findByRecipe(Recipe recipe);
}

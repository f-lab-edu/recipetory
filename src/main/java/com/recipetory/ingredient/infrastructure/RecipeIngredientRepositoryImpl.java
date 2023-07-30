package com.recipetory.ingredient.infrastructure;

import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.ingredient.domain.RecipeIngredientRepository;
import com.recipetory.recipe.domain.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecipeIngredientRepositoryImpl implements RecipeIngredientRepository {
    private final RecipeIngredientJpaRepository recipeIngredientJpaRepository;

    @Override
    public RecipeIngredient save(RecipeIngredient recipeIngredient) {
        return recipeIngredientJpaRepository.save(recipeIngredient);
    }

    @Override
    public List<RecipeIngredient> findByRecipe(Recipe recipe) {
        return recipeIngredientJpaRepository.findByRecipe(recipe);
    }
}

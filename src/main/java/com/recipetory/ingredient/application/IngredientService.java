package com.recipetory.ingredient.application;

import com.recipetory.ingredient.domain.Ingredient;
import com.recipetory.ingredient.domain.IngredientRepository;
import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.ingredient.presentation.dto.IngredientDto;
import com.recipetory.recipe.domain.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    // ingredient name 존재하면 찾아서 return, 없으면 save
    @Transactional
    public Ingredient saveOrFind(String ingredientName) {
        return ingredientRepository.findByName(ingredientName)
                .orElseGet(() -> ingredientRepository.save(
                        Ingredient.builder().name(ingredientName).build()));
    }

    // Recipe relation entity로 convert
    @Transactional
    public List<RecipeIngredient> convertToRelationEntity(
            Recipe recipe,
            List<IngredientDto> ingredients) {
        List<Ingredient> foundIngredients = ingredients.stream()
                .map(dto ->
                        saveOrFind(dto.getName()))
                .toList();

        return foundIngredients.stream().map(ingredient ->
                        RecipeIngredient.builder()
                                .recipe(recipe).ingredient(ingredient)
                                .build())
                .toList();
    }
}

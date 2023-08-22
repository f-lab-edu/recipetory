package com.recipetory.ingredient.application;

import com.recipetory.ingredient.domain.Ingredient;
import com.recipetory.ingredient.domain.IngredientRepository;

import java.util.*;

public class TestIngredientRepository implements IngredientRepository {
    private final List<Ingredient> ingredients = new ArrayList<>();

    @Override
    public Ingredient save(Ingredient ingredient) {
        ingredients.add(ingredient);
        return ingredient;
    }

    @Override
    public Optional<Ingredient> findByName(String name) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getName().equals(name))
                .findAny();
    }
}

package com.recipetory.ingredient.domain;

import java.util.Optional;

public interface IngredientRepository {
    Ingredient save(Ingredient ingredient);

    Optional<Ingredient> findByName(String name);
}

package com.recipetory.recipe.domain;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository {
    Optional<Recipe> findByTitle(String title);

    Recipe save(Recipe recipe);

    Optional<Recipe> findById(Long id);

    List<Recipe> findByTitleContains(String title);

    void deleteById(Long id);
}

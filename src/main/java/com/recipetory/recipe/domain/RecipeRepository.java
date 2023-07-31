package com.recipetory.recipe.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeRepository {
    Optional<Recipe> findByTitle(String title);

    Recipe save(Recipe recipe);

    Optional<Recipe> findById(Long id);
}

package com.recipetory.recipe.application;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TestRecipeRepository implements RecipeRepository {
    private final List<Recipe> recipes = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong(1L);

    @Override
    public Optional<Recipe> findByTitle(String title) {
        return recipes.stream()
                .filter(recipe -> recipe.getTitle().equals(title))
                .findAny();
    }

    @Override
    public Recipe save(Recipe recipe) {
        Recipe saved = Recipe.builder().author(recipe.getAuthor())
                .title(recipe.getTitle()).recipeStatistics(recipe.getRecipeStatistics())
                .recipeInfo(recipe.getRecipeInfo()).steps(recipe.getSteps())
                .ingredients(recipe.getIngredients()).tags(recipe.getTags())
                .recipeStatistics(recipe.getRecipeStatistics())
                .id(atomicLong.getAndIncrement()).build();

        // cascade
        saved.getSteps().forEach(step -> step.setRecipe(saved));
        saved.getTags().forEach(tag -> tag.setRecipe(saved));

        recipes.add(saved);
        return saved;
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return recipes.stream()
                .filter(recipe -> recipe.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Recipe> findByTitleContains(String title) {
        return recipes.stream()
                .filter(recipe -> recipe.getTitle().contains(title))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(recipes::remove);
    }
}

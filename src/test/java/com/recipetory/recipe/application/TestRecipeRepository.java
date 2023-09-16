package com.recipetory.recipe.application;

import com.recipetory.recipe.domain.*;
import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.tag.domain.Tag;
import com.recipetory.tag.domain.TagName;
import com.recipetory.user.domain.User;
import com.recipetory.utils.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TestRecipeRepository implements RecipeRepository {
    private final List<Recipe> recipes = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong(1L);

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
    public RecipeDocument getDocumentById(Long id) {
        return RecipeDocument.fromEntity(findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe", String.valueOf(id))));
    }

    @Override
    public List<RecipeDocument> findByRecipeInfo(String title, CookingTime cookingTime, Difficulty difficulty, Serving serving) {
        return recipes.stream().filter(recipe -> {
            RecipeInfo recipeInfo = recipe.getRecipeInfo();
            return recipe.getTitle().contains(title)
                    && (cookingTime.equals(CookingTime.UNDEFINED) || recipeInfo.getCookingTime().equals(cookingTime))
                    && (difficulty.equals(Difficulty.UNDEFINED) || recipeInfo.getDifficulty().equals(difficulty))
                    && (serving.equals(Serving.UNDEFINED) || recipeInfo.getServing().equals(serving));
        }).map(RecipeDocument::fromEntity).toList();
    }

    @Override
    public List<RecipeDocument> findByAuthor(User author) {
        return recipes.stream().filter(
                recipe -> recipe.getAuthor().equals(author))
                .map(RecipeDocument::fromEntity)
                .toList();
    }

    @Override
    public List<RecipeDocument> findByTagNames(List<TagName> tagNames) {
        return recipes.stream().filter(recipe ->
            recipe.getTags().stream().map(Tag::getTagName)
                    .toList().containsAll(tagNames))
                .map(RecipeDocument::fromEntity).toList();
    }

    // TODO : featured recipe test
    @Override
    public List<RecipeDocument> getFeatured() {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        recipes.stream().filter(recipe -> recipe.getId() == id)
                .findAny().ifPresent(recipes::remove);
    }
}

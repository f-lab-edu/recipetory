package com.recipetory.recipe.domain;

import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.tag.domain.TagName;
import com.recipetory.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository {
    Recipe save(Recipe recipe);

    Optional<Recipe> findById(Long id);

    RecipeDocument getDocumentById(Long id);

    List<RecipeDocument> findByRecipeInfo(
            String title,
            CookingTime cookingTime,
            Difficulty difficulty,
            Serving serving);

    List<RecipeDocument> findByAuthor(User author);

    List<RecipeDocument> findByTagNames(List<TagName> tagNames);

    List<RecipeDocument> getFeatured();

    void deleteById(Long id);
}

package com.recipetory.tag.domain;

import com.recipetory.recipe.domain.Recipe;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag save(Tag tag);

    List<Tag> findByRecipe(Recipe recipe);

    List<Tag> findByTagName(TagName tagName);

    Optional<Tag> findById(Long id);

    Optional<Tag> findByRecipeAndTagName(Recipe recipe, TagName tagName);

    void delete(Tag tag);
}

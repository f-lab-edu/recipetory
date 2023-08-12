package com.recipetory.tag.infrastructure;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.tag.domain.Tag;
import com.recipetory.tag.domain.TagName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<Tag,Long> {
    List<Tag> findByRecipe(Recipe recipe);

    List<Tag> findByTagName(TagName tagName);

    Optional<Tag> findByRecipeAndTagName(Recipe recipe, TagName tagName);
}

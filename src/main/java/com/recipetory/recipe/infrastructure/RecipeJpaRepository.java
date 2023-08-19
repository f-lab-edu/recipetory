package com.recipetory.recipe.infrastructure;

import com.recipetory.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeJpaRepository extends JpaRepository<Recipe,Long> {
    Optional<Recipe> findByTitle(String title);

    List<Recipe> findByTitleContains(String title);
}

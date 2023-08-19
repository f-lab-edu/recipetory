package com.recipetory.recipe.infrastructure;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepository {

    private final RecipeJpaRepository recipeJpaRepository;

    @Override
    public Optional<Recipe> findByTitle(String title) {
        return recipeJpaRepository.findByTitle(title);
    }

    @Override
    public Recipe save(Recipe recipe) {
        return recipeJpaRepository.save(recipe);
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return recipeJpaRepository.findById(id);
    }

    @Override
    public List<Recipe> findByTitleContains(String title) {
        return recipeJpaRepository.findByTitleContains(title);
    }

    @Override
    public void deleteById(Long id) {
        recipeJpaRepository.deleteById(id);
    }
}

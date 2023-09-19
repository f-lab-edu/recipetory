package com.recipetory.recipe.infrastructure;

import com.recipetory.recipe.domain.*;
import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.tag.domain.TagName;
import com.recipetory.user.domain.User;
import com.recipetory.utils.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepository {

    private final RecipeJpaRepository recipeJpaRepository;
    private final RecipeDocumentRepository recipeDocumentRepository;
    private final RecipeDocumentQueryService recipeQueryService;

    @Override
    public Recipe save(Recipe recipe) {
        return recipeJpaRepository.save(recipe);
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return recipeJpaRepository.findById(id);
    }

    @Override
    public RecipeDocument getDocumentById(Long id) {
        return recipeDocumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe", String.valueOf(id)));
    }

    @Override
    public List<RecipeDocument> findByRecipeInfo(
            String title, CookingTime cookingTime, Difficulty difficulty, Serving serving
    ) {
        return recipeQueryService.findByRecipeInfo(title, cookingTime, difficulty, serving);
    }

    @Override
    public List<RecipeDocument> findByAuthor(User author) {
        return recipeDocumentRepository.findTop100ByAuthorIdOrderByCreatedAt(author.getId());
    }

    @Override
    public List<RecipeDocument> findByTagNames(List<TagName> tagNames) {
        return recipeQueryService.findByTagNames(tagNames);
    }

    @Override
    public List<RecipeDocument> getFeatured() {
        return recipeQueryService.getTopRecipe(10);
    }

    @Override
    public void deleteById(Long id) {
        recipeJpaRepository.deleteById(id);
    }
}

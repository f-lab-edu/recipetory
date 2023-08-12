package com.recipetory.tag.infrastructure;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.tag.domain.Tag;
import com.recipetory.tag.domain.TagName;
import com.recipetory.tag.domain.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    private final TagJpaRepository tagJpaRepository;

    @Override
    public Tag save(Tag tag) {
        return tagJpaRepository.save(tag);
    }

    @Override
    public List<Tag> findByRecipe(Recipe recipe) {
        return tagJpaRepository.findByRecipe(recipe);
    }

    @Override
    public List<Tag> findByTagName(TagName tagName) {
        return tagJpaRepository.findByTagName(tagName);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return tagJpaRepository.findById(id);
    }

    @Override
    public Optional<Tag> findByRecipeAndTagName(Recipe recipe, TagName tagName) {
        return tagJpaRepository.findByRecipeAndTagName(recipe, tagName);
    }

    @Override
    public void delete(Tag tag) {
        tagJpaRepository.delete(tag);
    }
}

package com.recipetory.tag.application;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.tag.domain.Tag;
import com.recipetory.tag.domain.TagName;
import com.recipetory.tag.domain.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TestTagRepository implements TagRepository {
    private final List<Tag> tags = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong(1L);

    @Override
    public Tag save(Tag tag) {
        Tag saved = Tag.builder()
                .id(atomicLong.getAndIncrement())
                .recipe(tag.getRecipe()).tagName(tag.getTagName())
                .build();
        tags.add(saved);
        return saved;
    }

    @Override
    public List<Tag> findByRecipe(Recipe recipe) {
        return tags.stream().filter(tag -> tag.getRecipe().equals(recipe))
                .toList();
    }

    @Override
    public List<Tag> findByTagName(TagName tagName) {
        return tags.stream().filter(tag -> tag.getTagName().equals(tagName))
                .toList();
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return tags.stream().filter(tag -> tag.getId().equals(id))
                .findAny();
    }

    @Override
    public Optional<Tag> findByRecipeAndTagName(Recipe recipe, TagName tagName) {
        return tags.stream().filter(tag ->
                        tag.getRecipe().equals(recipe)
                                && tag.getTagName().equals(tagName))
                .findAny();
    }

    @Override
    public void delete(Tag tag) {
        tags.remove(tag);
    }
}

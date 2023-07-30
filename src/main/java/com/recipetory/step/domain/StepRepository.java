package com.recipetory.step.domain;

import com.recipetory.recipe.domain.Recipe;

import java.util.List;

public interface StepRepository {
    List<Step> findByRecipe(Recipe recipe);

    Step save(Step step);
}

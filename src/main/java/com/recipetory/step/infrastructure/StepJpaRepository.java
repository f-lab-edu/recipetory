package com.recipetory.step.infrastructure;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.step.domain.Step;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StepJpaRepository extends JpaRepository<Step, Long> {
    List<Step> findByRecipe(Recipe recipe);
}

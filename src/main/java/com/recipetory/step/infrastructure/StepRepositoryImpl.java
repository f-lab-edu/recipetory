package com.recipetory.step.infrastructure;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.step.domain.Step;
import com.recipetory.step.domain.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StepRepositoryImpl implements StepRepository {

    private final StepJpaRepository stepJpaRepository;

    @Override
    public List<Step> findByRecipe(Recipe recipe) {
        return stepJpaRepository.findByRecipe(recipe);
    }

    @Override
    public Step save(Step step) {
        return stepJpaRepository.save(step);
    }
}

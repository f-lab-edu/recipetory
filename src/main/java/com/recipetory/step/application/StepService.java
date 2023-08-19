package com.recipetory.step.application;

import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.step.domain.Step;
import com.recipetory.step.domain.StepRepository;
import com.recipetory.step.presentation.dto.StepListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StepService {
    private final StepRepository stepRepository;
    private final RecipeService recipeService;

    /**
     * Recipe에 해당하는 스텝 list를 조회한다.
     * @param recipeId
     * @return
     */
    @Transactional
    public StepListDto getStepsByRecipeId(Long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        List<Step> found = stepRepository.findByRecipe(recipe);

        return StepListDto.fromEntityList(found);
    }
}

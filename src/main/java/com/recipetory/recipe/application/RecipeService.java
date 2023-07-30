package com.recipetory.recipe.application;

import com.recipetory.ingredient.application.IngredientService;
import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.ingredient.presentation.dto.RecipeIngredientDto;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final UserRepository userRepository;

    @Transactional
    public Recipe createRecipe(Recipe recipe,
                               List<RecipeIngredientDto> ingredients,
                               String authorEmail) {
        // 1. validate user role
        User author = findUserByEmail(authorEmail);
        author.verifyUserHasRole(Role.USER);

        // 2. ingredient -> recipeIngredient
        List<RecipeIngredient> recipeIngredients =
                ingredientService.convertToRelationEntity(
                        recipe, ingredients);

        // 3. set basic relations
        recipe.setBasicRelations(author,recipeIngredients);
        recipe.getSteps().forEach(step -> step.setRecipe(recipe));

        // 4. save(persist) -> cascade
        return recipeRepository.save(recipe);
    }

    @Transactional
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}

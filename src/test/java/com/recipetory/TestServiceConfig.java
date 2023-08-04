package com.recipetory;

import com.recipetory.ingredient.application.IngredientService;
import com.recipetory.ingredient.domain.IngredientRepository;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestServiceConfig {
    @Bean
    public RecipeService recipeService(
            RecipeRepository recipeRepository,
            IngredientService ingredientService,
            UserRepository userRepository) {

        return new RecipeService(recipeRepository, ingredientService, userRepository);
    }

    @Bean
    public IngredientService ingredientService(
            IngredientRepository ingredientRepository) {
        return new IngredientService(ingredientRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }
}

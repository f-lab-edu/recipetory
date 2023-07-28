package com.recipetory.ingredient.application;

import com.recipetory.ingredient.domain.IngredientRepository;
import com.recipetory.ingredient.infrastructure.IngredientJpaRepository;
import com.recipetory.ingredient.infrastructure.IngredientRepositoryImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class IngredientTestConfig {
    @Bean
    public IngredientRepository ingredientRepository(
            IngredientJpaRepository ingredientJpaRepository) {
        return new IngredientRepositoryImpl(ingredientJpaRepository);
    }

    @Bean
    public IngredientService ingredientService(
            IngredientRepository ingredientRepository) {
        return new IngredientService(ingredientRepository);
    }
}

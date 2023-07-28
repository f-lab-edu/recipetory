package com.recipetory.ingredient.infrastructure;

import com.recipetory.ingredient.domain.Ingredient;
import com.recipetory.ingredient.domain.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IngredientRepositoryImpl implements IngredientRepository {
    private final IngredientJpaRepository ingredientJpaRepositoryRepository;

    @Override
    public Ingredient save(Ingredient ingredient) {
        return ingredientJpaRepositoryRepository.save(ingredient);
    }

    @Override
    public Optional<Ingredient> findByName(String name) {
        return ingredientJpaRepositoryRepository.findByName(name);
    }
}

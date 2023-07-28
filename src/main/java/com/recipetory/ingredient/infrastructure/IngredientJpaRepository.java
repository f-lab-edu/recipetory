package com.recipetory.ingredient.infrastructure;

import com.recipetory.ingredient.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientJpaRepository extends JpaRepository<Ingredient,Long> {
    Optional<Ingredient> findByName(String name);
}

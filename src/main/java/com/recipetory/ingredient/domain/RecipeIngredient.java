package com.recipetory.ingredient.domain;

import com.recipetory.recipe.domain.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "recipe_ingredient")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Recipe.class)
    @JoinColumn(name = "recipe_id",
            nullable = false)
    private Recipe recipe;

    @ManyToOne(targetEntity = Ingredient.class)
    @JoinColumn(name = "ingredient_id",
            nullable = false)
    private Ingredient ingredient;
}

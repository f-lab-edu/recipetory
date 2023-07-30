package com.recipetory.ingredient.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,
            length = 20)
    private String name;

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 100)
    @Builder.Default
    private String description = "";

    @OneToMany(targetEntity = RecipeIngredient.class,
            mappedBy = "ingredient",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RecipeIngredient> recipes;
}

package com.recipetory.recipe.domain;

import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.step.domain.Step;
import com.recipetory.user.domain.User;
import com.recipetory.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Recipe extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @JoinColumn(name = "author_id",
            nullable = false)
    @ManyToOne(targetEntity = User.class)
    private User author;

    @Embedded
    private RecipeInfo recipeInfo;

    @Embedded
    private RecipeStatistics recipeStatistics;

    @OneToMany(targetEntity = Step.class,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "recipe")
    private List<Step> steps;

    @OneToMany(targetEntity = RecipeIngredient.class,
            cascade = CascadeType.ALL,
            mappedBy = "recipe",
            orphanRemoval = true)
    private List<RecipeIngredient> ingredients;

    // set basic relations for creation
    public void setBasicRelations(User author,
                                  List<RecipeIngredient> ingredients) {
        this.author = author;
        this.ingredients = ingredients;
    }
}

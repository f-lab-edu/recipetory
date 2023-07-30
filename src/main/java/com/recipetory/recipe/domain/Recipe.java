package com.recipetory.recipe.domain;

import com.recipetory.bookmark.domain.exception.CannotBookMarkException;
import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.step.domain.Step;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.exception.NotOwnerException;
import com.recipetory.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Recipe extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,
            length = 30)
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

    public boolean isSameAuthor(User user) {
        return author.getId().equals(user.getId());
    }
}

package com.recipetory.recipe.domain;

import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.step.domain.Step;
import com.recipetory.tag.domain.Tag;
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
    @OrderBy("stepNumber asc")
    private List<Step> steps;

    @OneToMany(targetEntity = RecipeIngredient.class,
            cascade = CascadeType.ALL,
            mappedBy = "recipe",
            orphanRemoval = true)
    private List<RecipeIngredient> ingredients;

    @OneToMany(targetEntity = BookMark.class,
            cascade = CascadeType.REMOVE,
            mappedBy = "recipe",
            orphanRemoval = true)
    private List<BookMark> bookMarks;

    @OneToMany(targetEntity = Tag.class,
            cascade = CascadeType.ALL,
            mappedBy = "recipe",
            orphanRemoval = true)
    private List<Tag> tags;

    // set basic relations for creation
    public void setBasicRelations(User author,
                                  List<RecipeIngredient> ingredients) {
        this.author = author;
        this.ingredients = ingredients;
    }

    public boolean isSameAuthor(User user) {
        return author.getId().equals(user.getId());
    }

    public void addBookMarkCount() {
        recipeStatistics.addBookMarkCount();
    }
    public void subtractBookMarkCount() {
        recipeStatistics.subtractBookMarkCount();
    }
}

package com.recipetory.bookmark.domain;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.user.domain.User;
import com.recipetory.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BookMark extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Recipe.class)
    @JoinColumn(nullable = false)
    private Recipe recipe;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false)
    private User bookMarker;
}

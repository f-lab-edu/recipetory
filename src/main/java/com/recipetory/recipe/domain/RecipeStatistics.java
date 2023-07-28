package com.recipetory.recipe.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecipeStatistics {
    private int ratings;

    private int viewCount;

    private int bookMarkCount;
}

package com.recipetory.recipe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecipeStatistics {
    @Column
    private int ratings;

    @Column
    private int viewCount;

    @Column
    private int bookMarkCount;
    
    public void addBookMarkCount() {
        this.bookMarkCount += 1;
    }
    public void subtractBookMarkCount() {
        this.bookMarkCount -= 1;
    }

    public String getRatingFormat() {
        return String.format("%.1f",ratings / 10.0);
    }
}

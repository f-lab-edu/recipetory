package com.recipetory.recipe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * 레시피 평점
 * 조회, 북마크, 댓글, 리뷰 수 등
 * 레시피 관련한 statistic 정보를 갖는 밸류
 * */
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

    @Column
    private int commentCount;

    @Column
    private int reviewCount;

    public void updateCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void updateReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void updateRatings(int ratingSum) {
        this.ratings = (reviewCount == 0) ? 0 : ratingSum / reviewCount;
    }

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

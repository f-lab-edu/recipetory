package com.recipetory.reply.presentation.review.dto;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.user.domain.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateReviewDto {
    @NotNull(message = "리뷰를 추가할 레시피 아이디가 존재해야 합니다.")
    private Long recipeId;

    @NotNull
    @Min(value = 10, message = "별점은 10 ~ 50 사이의 값이어야 합니다.")
    @Max(value = 50, message = "별점은 10 ~ 50 사이의 값이어야 합니다.")
    private int rating;

    @NotNull(message = "생성할 리뷰 본문이 존재하지 않습니다.")
    @NotBlank(message = "생성할 리뷰가 공란이어선 안됩니다.")
    @Length(max = Review.MAX_CONTENT_LENGTH,
            message = "리뷰는 최대" + Review.MAX_CONTENT_LENGTH + "자까지 가능합니다.")
    private String content;

    public Review toEntity(User author, Recipe recipe) {
        return Review.builder()
                .author(author)
                .recipe(recipe)
                .rating(rating).content(content)
                .build();
    }
}

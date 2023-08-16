package com.recipetory.reply.presentation.review.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.presentation.dto.SimpleRecipeDto;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.user.domain.User;
import com.recipetory.user.presentation.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDto {
    private Long reviewId;
    private UserDto author;
    private SimpleRecipeDto recipe;
    private String rating;
    private String content;

    public static ReviewDto fromEntity(Review review) {
        User author = review.getAuthor();
        Recipe recipe = review.getRecipe();

        return ReviewDto.builder()
                .reviewId(review.getId())
                .author(UserDto.fromEntity(author))
                .recipe(SimpleRecipeDto.fromEntity(recipe))
                .rating(review.getRatingFormat())
                .content(review.getContent())
                .build();
    }
}

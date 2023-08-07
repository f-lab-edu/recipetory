package com.recipetory.reply.presentation.review.dto;

import com.recipetory.reply.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewListDto {
    private List<ReviewDto> reviews;

    public static ReviewListDto fromEntityList(List<Review> reviews) {
        List<ReviewDto> reviewDtos = reviews.stream()
                .map(ReviewDto::fromEntity).toList();

        return new ReviewListDto(reviewDtos);
    }
}

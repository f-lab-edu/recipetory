package com.recipetory.reply.presentation.review.dto;

import com.recipetory.reply.domain.review.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateReviewDto {
    @Min(value = 10, message = "별점은 10 ~ 50 사이의 값이어야 합니다.")
    @Max(value = 50, message = "별점은 10 ~ 50 사이의 값이어야 합니다.")
    private int rating;

    @NotBlank(message = "리뷰가 공란이어선 안됩니다.")
    @Length(min = 1,
            max = Review.MAX_CONTENT_LENGTH,
            message = "리뷰는 2000자 미만이어야 합니다.")
    private String content;
}

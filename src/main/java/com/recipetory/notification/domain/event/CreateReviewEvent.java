package com.recipetory.notification.domain.event;

import com.recipetory.reply.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateReviewEvent {
    private final Review review;
}

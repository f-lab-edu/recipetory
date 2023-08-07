package com.recipetory.reply.presentation.review;

import com.recipetory.config.auth.argumentresolver.LogInUser;
import com.recipetory.config.auth.dto.SessionUser;
import com.recipetory.reply.application.ReviewService;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.reply.presentation.review.dto.CreateReviewDto;
import com.recipetory.reply.presentation.review.dto.ReviewDto;
import com.recipetory.reply.presentation.review.dto.ReviewListDto;
import com.recipetory.reply.presentation.review.dto.UpdateReviewDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰를 생성한다.
     * @param request request {@link CreateReviewDto}
     * @param logInUser 현재 로그인한 user id
     * @return 생성된 리뷰 response body
     */
    @PostMapping("/reviews")
    public ResponseEntity<ReviewDto> createReview(
            @Valid @RequestBody CreateReviewDto request,
            @LogInUser SessionUser logInUser) {
        Review created = reviewService.createReview(
                logInUser.getEmail(), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReviewDto.fromEntity(created));
    }

    /**
     * 특정 id의 리뷰를 조회한다.
     * @param reviewId reviewId
     * @return found review
     */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> readReview(
            @PathVariable("reviewId") Long reviewId) {

        Review found = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(ReviewDto.fromEntity(found));
    }

    /**
     * 특정 레시피에 작성된 리뷰를 조회한다.
     * @param recipeId {@link PathVariable}로 주어진 레시피 id
     * @return 레시피에 작성된 리뷰 목록 response body
     */
    @GetMapping("/recipe/{recipeId}/reviews")
    public ResponseEntity<ReviewListDto> getReviewList(
            @PathVariable("recipeId") Long recipeId) {

        List<Review> reviews = reviewService.getReviewByRecipeId(recipeId);
        ReviewListDto reviewDtos = ReviewListDto.fromEntityList(reviews);
        return ResponseEntity.ok(reviewDtos);
    }

    /**
     * 특정 유저가 작성한 리뷰들을 조회한다.
     * @param userId {@link PathVariable}로 주어진 유저 id
     * @return 유저가 작성한 리뷰 목록 response body
     */
    @GetMapping("/{userId}/reviews")
    public ResponseEntity<ReviewListDto> getReviewsOfUser(
            @PathVariable("userId") Long userId) {

        List<Review> reviews = reviewService.getReviewByUserId(userId);
        ReviewListDto reviewDtos = ReviewListDto.fromEntityList(reviews);
        return ResponseEntity.ok(reviewDtos);
    }

    /**
     * 리뷰를 수정한다.
     * @param reviewId 수정하고자 하는 리뷰 id
     * @param logInUser 현재 로그인된 {@link SessionUser}. 리뷰 작성자와 같아야한다.
     * @param request request dto
     * @return 수정된 리뷰 response body
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable("reviewId") Long reviewId,
            @LogInUser SessionUser logInUser,
            @Valid @RequestBody UpdateReviewDto request) {

        Review updated = reviewService.updateReview(
                logInUser.getEmail(), reviewId, request);
        return ResponseEntity.ok(ReviewDto.fromEntity(updated));
    }

    /**
     * 리뷰를 삭제한다.
     * @param reviewId 삭제하고자 하는 리뷰 id
     * @param logInUser 현재 로그인된 {@link SessionUser}. 리뷰의 author와 같아야한다.
     * @return empty body
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("reviewId") Long reviewId,
            @LogInUser SessionUser logInUser) {

        reviewService.deleteReview(logInUser.getEmail(), reviewId);
        return ResponseEntity.ok().build();
    }
}

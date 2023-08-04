package com.recipetory.reply.application;

import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.exception.CannotReviewException;
import com.recipetory.reply.domain.exception.ReplyNotFoundException;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.reply.domain.review.ReviewRepository;
import com.recipetory.reply.presentation.review.dto.CreateReviewDto;
import com.recipetory.reply.presentation.review.dto.UpdateReviewDto;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RecipeService recipeService;
    private final UserService userService;

    /**
     * 레시피에 대한 리뷰를 추가한다.
     * @param logInEmail 리뷰를 작성하는 유저 email
     * @param reviewDto 생성하는 {@link CreateReviewDto}
     * @return 생성되어 DB에 저장된 리뷰
     */
    @Transactional
    public Review createReview(String logInEmail,
                               CreateReviewDto reviewDto) {
        User author = userService.getUserByEmail(logInEmail);
        Recipe recipe = recipeService.getRecipeById(reviewDto.getRecipeId());

        if (recipe.isSameAuthor(author)) {
            throw new CannotReviewException(author.getId(), recipe.getId());
        }

        Review created = reviewRepository.save(
                reviewDto.toEntity(author,recipe));
        updateReviewStatistic(recipe);

        return created;
    }

    /**
     * 특정 id를 가진 레시피에 달린 리뷰를 조회한다.
     * @param recipeId 레시피 id
     * @return 레시피에 달린 리뷰 List
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewByRecipeId(Long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return reviewRepository.findByRecipe(recipe);
    }

    /**
     * 특정 유저가 작성한 리뷰를 조회한다.
     * @param userId 유저 id
     * @return 유저가 작성한 리뷰 List
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewByUserId(Long userId) {
        User author = userService.getUserById(userId);
        return reviewRepository.findByAuthor(author);
    }

    /**
     * 특정 id를 가진 리뷰 정보를 업데이트한다.
     * @param logInEmail 리뷰 작성자 email
     * @param reviewId 수정하고자 하는 리뷰 id
     * @param reviewDto 수정하고자 하는 리뷰 content
     * @return 수정된 리뷰
     */
    @Transactional
    public Review updateReview(String logInEmail,
                               Long reviewId,
                               UpdateReviewDto reviewDto) {
        Review foundReview = getReviewById(reviewId);
        User currentUser = userService.getUserByEmail(logInEmail);

        foundReview.verifyAuthor(currentUser);
        foundReview.update(reviewDto);
        updateReviewStatistic(foundReview.getRecipe());

        return foundReview;
    }

    /**
     * 해당하는 id의 리뷰를 삭제한다.
     * @param logInEmail 리뷰 주인 유저의 이메일
     * @param reviewId 리뷰 id
     */
    @Transactional
    public void deleteReview(String logInEmail,
                             Long reviewId) {
        Review foundReview = getReviewById(reviewId);
        User currentUser = userService.getUserByEmail(logInEmail);

        foundReview.verifyAuthor(currentUser);
        reviewRepository.delete(foundReview);
        updateReviewStatistic(foundReview.getRecipe());
    }

    /**
     * 특정 id를 가진 리뷰를 조회한다.
     * @param reviewId id of review
     * @return found review
     * @throws ReplyNotFoundException 존재하지 않는 리뷰에 대해 발생한다.
     */
    @Transactional(readOnly = true)
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(ReplyNotFoundException::new);
    }

    /**
     * Review 관련한 Recipe statistics 정보를 업데이트한다.
     * 리뷰 조회 혹은 레시피 조회 시에는 호출되지 않는다.
     * @param recipe 업데이트되는 레시피
     */
    @Transactional
    public void updateReviewStatistic(Recipe recipe) {
        List<Review> reviews = reviewRepository.findByRecipe(recipe);
        int reviewCount = reviews.size();
        int ratingSum = reviews.stream().mapToInt(Review::getRating).sum();

        recipe.getRecipeStatistics().updateReviewCount(reviewCount);
        recipe.getRecipeStatistics().updateRatings(ratingSum);
    }
}

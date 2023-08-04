package com.recipetory.reply.application;

import com.recipetory.TestRepositoryConfig;
import com.recipetory.TestServiceConfig;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.reply.domain.exception.CannotReviewException;
import com.recipetory.reply.domain.exception.ReplyNotFoundException;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.reply.domain.review.ReviewRepository;
import com.recipetory.reply.presentation.review.dto.CreateReviewDto;
import com.recipetory.reply.presentation.review.dto.UpdateReviewDto;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.exception.NotOwnerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestRepositoryConfig.class, TestServiceConfig.class})
public class ReviewServiceTest {
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    User recipeAuthor, reviewAuthor;
    Recipe recipe;
    String recipeAuthorEmail, reviewAuthorEmail;

    // 레시피 주인, 리뷰 주인, 레시피를 저장한다.
    @BeforeEach
    public void setUp() {
        reviewService = new ReviewService(
                reviewRepository, recipeService, userService);

        recipeAuthorEmail = "recipe@test.com";
        recipeAuthor = userRepository.save(
                User.builder()
                        .name("recipeAuthor").email(recipeAuthorEmail).role(Role.USER)
                        .build());
        recipe = recipeRepository.save(
                Recipe.builder()
                        .title("recipe").recipeStatistics(new RecipeStatistics()).author(recipeAuthor)
                        .build());

        reviewAuthorEmail = "review@test.com";
        reviewAuthor = userRepository.save(
                User.builder()
                        .name("reviewAuthor").email(reviewAuthorEmail).role(Role.USER)
                        .build());

    }


    @Test
    @DisplayName("레시피의 주인은 리뷰를 작성할 수 없다.")
    public void cannotReviewTest() {
        CreateReviewDto request = getCreateRequest();

        assertThrows(CannotReviewException.class, () ->
                reviewService.createReview(recipeAuthorEmail,request));
    }

    @Test
    @DisplayName("작성한 리뷰는 조회될 수 있다.")
    public void createReviewTest() {
        // given, when : 리뷰를 작성한다.
        CreateReviewDto request = getCreateRequest();
        Review created = reviewService.createReview(reviewAuthorEmail,request);

        // then : reviewId, recipeId, userId로 리뷰가 조회된다.
        assertEquals(created, reviewService.getReviewById(created.getId()));
        assertTrue(reviewService.getReviewByRecipeId(recipe.getId()).contains(created));
        assertTrue(reviewService.getReviewByUserId(reviewAuthor.getId()).contains(created));
    }

    @Test
    @DisplayName("작성된 리뷰가 레시피 정보(리뷰 수, 평점)에 반영된다.")
    public void recipeStatisticsTest() {
        // given
        int reviewCount = 10;
        List<Integer> ratings = new ArrayList<>();
        Random random = new Random();

        // when : 랜덤한 평점을 가진 리뷰를 reviewCount만큼 작성한다.
        for (int i = 0; i < reviewCount; i++) {
            int rating = random.nextInt(41) + 10;
            ratings.add(rating);

            CreateReviewDto request = new CreateReviewDto(
                    recipe.getId(), rating, "review content");
            reviewService.createReview(reviewAuthor.getEmail(),request);
        }

        // then : 평점과 리뷰 갯수가 레시피에 반영된다.
        assertEquals(reviewCount, recipe.getRecipeStatistics().getReviewCount());

        int expectedRatings = ratings.stream().reduce(0, Integer::sum) / reviewCount;
        assertEquals(expectedRatings, recipe.getRecipeStatistics().getRatings());
    }

    @Test
    @DisplayName("리뷰 작성자가 아닌 사용자는 리뷰를 수정하거나 삭제할 수 없다.")
    public void invalidUserReviewTest() {
        // given 1 : reviewAuthor 유저가 리뷰를 작성한다.
        CreateReviewDto request = getCreateRequest();
        Review created = reviewService.createReview(reviewAuthorEmail, request);

        // given 2 : 리뷰 작성자가 아닌 another 유저가 존재한다.
        User another = userRepository.save(User.builder()
                .email("another@test.com").name("another").role(Role.USER).build());

        // when, then : another 유저는 댓글 수정이나 삭제가 불가하다.
        assertThrows(NotOwnerException.class, () ->
                reviewService.updateReview(another.getEmail(), created.getId(), new UpdateReviewDto()));
        assertThrows(NotOwnerException.class, () ->
                reviewService.deleteReview(another.getEmail(),created.getId()));
    }

    @Test
    @DisplayName("리뷰는 작성자에 의해 수정될 수 있다.")
    public void updateReviewTest() {
        // given : reviewAuthor 유저가 리뷰를 작성한다.
        CreateReviewDto request = getCreateRequest();
        Review created = reviewService.createReview(reviewAuthorEmail, request);

        // when :  reviewAuthor 유저가 작성된 리뷰를 수정한다.
        UpdateReviewDto updateRequest = new UpdateReviewDto(10, "new review content");
        Review updated = reviewService.updateReview(reviewAuthorEmail, created.getId(), updateRequest);

        // then : 리뷰에 대한 평점과 내용이 updatedRequest 내용과 동일하다.
        assertEquals(created, updated);
        assertEquals(updateRequest.getRating(), updated.getRating());
        assertEquals(updateRequest.getContent(), updated.getContent());
    }

    @Test
    @DisplayName("리뷰는 작성자에 의해 삭제될 수 있다.")
    public void deleteReviewTest() {
        // given : reviewAuthor 유저가 리뷰를 작성한다.
        CreateReviewDto request = getCreateRequest();
        Review created = reviewService.createReview(reviewAuthorEmail, request);

        // when :  reviewAuthor 유저가 작성된 리뷰를 삭제한다.
        reviewService.deleteReview(reviewAuthorEmail, created.getId());

        // then : 리뷰 id로 조회하면 ReplyNotFoundException이 발생한다.
        assertThrows(ReplyNotFoundException.class, () ->
                reviewService.getReviewById(created.getId()));
    }

    @Test
    @DisplayName("레시피의 리뷰가 모두 삭제되면(개수가 0개면) 평점은 0점이다.")
    public void zeroRatingsTest() {
        // given : 리뷰를 작성한다.
        CreateReviewDto request = getCreateRequest();
        Review created = reviewService.createReview(reviewAuthorEmail,request);

        // when : 리뷰를 삭제한다.
        reviewService.deleteReview(reviewAuthorEmail, created.getId());

        // then : 레시피의 리뷰 갯수는 0개이고, 평점은 0점이다.
        assertEquals(0, recipe.getRecipeStatistics().getReviewCount());
        assertEquals(0, recipe.getRecipeStatistics().getRatings());
    }

    /**
     * 테스트에 사용할 리뷰 생성 request dto를 반환한다.
     * @return {@link CreateReviewDto}
     */
    private CreateReviewDto getCreateRequest() {
        return new CreateReviewDto(
                recipe.getId(),30,"test review");
    }
}

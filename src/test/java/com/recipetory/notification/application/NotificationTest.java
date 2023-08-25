package com.recipetory.notification.application;

import com.recipetory.notification.domain.Notification;
import com.recipetory.notification.domain.NotificationRepository;
import com.recipetory.notification.domain.NotificationType;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeInfo;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.reply.application.CommentService;
import com.recipetory.reply.application.ReviewService;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.reply.presentation.comment.dto.CreateCommentDto;
import com.recipetory.reply.presentation.review.dto.CreateReviewDto;
import com.recipetory.user.application.FollowService;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@EmbeddedKafka(partitions = 4,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092" })
// @DirtiesContext : spring boot test에서 application context를 재사용하는 것을 막아줌
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NotificationTest {
    @Autowired
    private FollowService followService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("레시피를 작성하면 팔로워들에게 알림이 생성된다.")
    public void createdRecipeNotificationTest() {
        // given : author 유저를 follower가 팔로우한다.
        User author = userRepository.save(User.builder()
                .name("author").email("author@test.com").role(Role.USER).build());
        User follower = userRepository.save(User.builder()
                .name("follower1").email("follower1@test.com").role(Role.USER).build());
        followService.follow(follower.getEmail(),author.getId());

        // when : author 유저가 새로운 레시피를 작성한다.
        Recipe recipe = Recipe.builder()
                .title("test").recipeInfo(new RecipeInfo()).recipeStatistics(new RecipeStatistics())
                .steps(new ArrayList<>()).tags(new ArrayList<>()).ingredients(new ArrayList<>())
                .build();
        recipeService.createRecipe(recipe,new ArrayList<>(),author.getEmail());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // then : sender가 author, receiver가 follower인 NEW_RECIPE 알림이 1개 생성된다.
        await().atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertEquals(1,
                            notificationRepository.findByReceiver(follower).size()); // 1개
                });
        Notification notification = notificationRepository.findByReceiver(follower).get(0);
        assertEquals(author.getId(),notification.getSender().getId()); // sender
        assertEquals(NotificationType.NEW_RECIPE, notification.getNotificationType()); // NEW_RECIPE
    }

    @Test
    @DisplayName("팔로우를 하면 팔로우 당한 사람에게 알림이 생성된다.")
    public void followNotificationTest() {
        // given : 팔로우 당하는 followed, 팔로우 하는 follower 유저가 존재한다.
        User followed = userRepository.save(User.builder()
                .name("author").email("author@test.com").role(Role.USER).build());
        User follower = userRepository.save(User.builder()
                .name("follower1").email("follower1@test.com").role(Role.USER).build());

        // when : follower 유저가 followed 유저를 팔로우한다.
        followService.follow(follower.getEmail(),followed.getId());

        // TransactionalEventListener를 사용중이기 때문에, 이전 transaction commit 필요
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // then : sender가 follower, receiver가 followed인 FOLLOW 알림이 1개 생성된다.
        await().atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertEquals(1,
                            notificationRepository.findByReceiver(followed).size()); // 1개
                });

        Notification notification = notificationRepository.findByReceiver(followed).get(0);
        assertEquals(follower.getId(), notification.getSender().getId()); // sender
        assertEquals(NotificationType.FOLLOW, notification.getNotificationType()); // FOLLOW
    }

    @Test
    @DisplayName("레시피 작성자에게 댓글 작성 알림이 생성된다.")
    public void commentNotificationTest() {
        // given : recipeAuthor 유저가 recipe 레시피를 생성한다.
        User recipeAuthor = userRepository.save(User.builder()
                .name("recipeAuthor").email("recipeAutho@test.com").role(Role.USER).build());
        Recipe recipe = Recipe.builder()
                .title("test").recipeInfo(new RecipeInfo()).recipeStatistics(new RecipeStatistics())
                .steps(new ArrayList<>()).tags(new ArrayList<>()).ingredients(new ArrayList<>())
                .build();
        recipeService.createRecipe(recipe,new ArrayList<>(),recipeAuthor.getEmail());

        // when : commentAuthor 유저가 댓글을 생성한다.
        User commentAuthor = userRepository.save(User.builder()
                .name("commentAuthor").email("commentAuthor@test.com").role(Role.USER).build());
        Comment comment = commentService.createComment(commentAuthor.getEmail(), new CreateCommentDto(
                recipe.getId(),"test comment"));

        // TransactionalEventListener 사용중이기 때문에, 이전 transaction commit 필요
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // then : sender가 commentAuthor, receiver가 recipeAuthor인 COMMENT 알림이 1개 생성된다.
        await().atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertEquals(1,
                            notificationRepository.findByReceiver(recipeAuthor).size()); // 1개
                });

        Notification notification = notificationRepository.findByReceiver(recipeAuthor).get(0);
        assertEquals(commentAuthor.getId(), notification.getSender().getId()); // sender
        assertEquals(NotificationType.COMMENT, notification.getNotificationType()); // COMMENT
    }

    @Test
    @DisplayName("레시피 작성자에게 리뷰 작성 알림이 생성된다.")
    public void reviewNotificationTest() {
        // given : recipeAuthor 유저가 레시피를 생성한다.
        User recipeAuthor = userRepository.save(User.builder()
                .name("recipeAuthor").email("recipeAutho@test.com").role(Role.USER).build());
        Recipe recipe = Recipe.builder()
                .title("test").recipeInfo(new RecipeInfo()).recipeStatistics(new RecipeStatistics())
                .steps(new ArrayList<>()).tags(new ArrayList<>()).ingredients(new ArrayList<>())
                .build();
        recipeService.createRecipe(recipe,new ArrayList<>(),recipeAuthor.getEmail());

        // when : reviewAuthor 유저가 리뷰를 생성한다.
        User reviewAuthor = userRepository.save(User.builder()
                .name("reviewAuthor").email("reviewAuthor@test.com").role(Role.USER).build());
        Review review = reviewService.createReview(reviewAuthor.getEmail(), new CreateReviewDto(
                recipe.getId(),30,"test review"));

        // TransactionalEventListener 사용중이기 때문에, 이전 transaction commit 필요
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // then : sender가 reviewAuthor, receiver가 recipeAuthor인 COMMENT 알림이 1개 생성된다.
        await().atMost(30, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertEquals(1,
                            notificationRepository.findByReceiver(recipeAuthor).size()); // 1개
                });

        Notification notification = notificationRepository.findByReceiver(recipeAuthor).get(0);
        assertEquals(reviewAuthor.getId(), notification.getSender().getId());
        assertEquals(NotificationType.REVIEW, notification.getNotificationType());
    }
}
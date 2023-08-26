package com.recipetory.notification.application;

import com.recipetory.notification.domain.Notification;
import com.recipetory.notification.domain.NotificationRepository;
import com.recipetory.notification.domain.NotificationType;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeInfo;
import com.recipetory.recipe.domain.RecipeStatistics;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@EmbeddedKafka(partitions = 4,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092" })
// @DirtiesContext : spring boot test에서 application context를 재사용하는 것을 막아줌
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NotificationTest {
    @Autowired
    private FollowService followService;
    @Autowired
    private RecipeService recipeService;
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

        // then : sender가 author, receiver가 follower인 NEW_RECIPE 알림이 1개
        //        sender가 follower, receiver가 author인 FOLLOW 알림이 1개 생성된다.
        await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertAll(
                            () -> assertEquals(
                                    1, notificationRepository.findByReceiver(follower).size()),
                            () -> assertEquals(
                                    1, notificationRepository.findByReceiver(author).size()));
                });

        Notification createRecipenotification = notificationRepository.findByReceiver(follower).get(0);
        assertEquals(author.getId(),createRecipenotification.getSender().getId()); // sender
        assertEquals(NotificationType.NEW_RECIPE, createRecipenotification.getNotificationType()); // NEW_RECIPE

        Notification followNotification = notificationRepository.findByReceiver(author).get(0);
        assertEquals(follower.getId(), followNotification.getSender().getId());
        assertEquals(NotificationType.FOLLOW, followNotification.getNotificationType());
    }
}
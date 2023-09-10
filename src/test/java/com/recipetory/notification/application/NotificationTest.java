package com.recipetory.notification.application;

import com.recipetory.notification.domain.NotificationMessageSender;
import com.recipetory.notification.domain.NotificationType;
import com.recipetory.notification.presentation.dto.NotificationMessage;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeInfo;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.user.application.FollowService;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.follow.Follow;
import com.recipetory.user.domain.follow.FollowRepository;
import com.recipetory.user.service.TestFollowRepository;
import com.recipetory.user.service.TestUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@AutoConfigureTestDatabase
@EmbeddedKafka(brokerProperties = {
        "listeners=PLAINTEXT://localhost:9093" })
class NotificationTest {
    @MockBean
    private NotificationMessageSender notificationMessageSender;
    @Autowired
    private FollowService followService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowRepository followRepository;

    @Test
    @Transactional
    @DisplayName("레시피를 작성하면 팔로워들에게 알림 메세지가 발송된다.")
    public void createRecipeNotificationTest() {
        // given : author 유저와 그를 팔로우하는 follower 유저가 존재한다.
        User author = userRepository.save(User.builder()
                .name("author").email("author@test.com").role(Role.USER).build());
        User follower = userRepository.save(User.builder()
                .name("follower").email("follower@test.com").role(Role.USER).build());
        followRepository.save(Follow.builder()
                .followed(author).following(follower).build());

        // when : author 유저가 레시피를 작성한다.
        Recipe recipe = Recipe.builder()
                .title("test").recipeInfo(new RecipeInfo()).recipeStatistics(new RecipeStatistics())
                .steps(new ArrayList<>()).tags(new ArrayList<>()).ingredients(new ArrayList<>())
                .build();
        recipeService.createRecipe(recipe, new ArrayList<>(), author.getEmail());
        // @TransactionalEventListener의 AFTER_COMMIT을 위한 end 처리
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // then : sender가 author인 알림이 발송된다.
        ArgumentCaptor<NotificationMessage> argumentCaptor =
                ArgumentCaptor.forClass(NotificationMessage.class);
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
            // Awaitility를 이용한 비동기 테스트
            verify(notificationMessageSender)
                    .sendNotificationMessage(argumentCaptor.capture());
        });
        NotificationMessage sent = argumentCaptor.getValue();
        assertEquals(NotificationType.NEW_RECIPE, sent.getNotificationType());
        assertEquals(author.getId(), sent.getSenderId());
    }

    @Test
    @Transactional
    @DisplayName("특정 유저를 팔로우하면 팔로우 알림 생성 메세지가 발송된다.")
    public void followNotificationTest() {
        // given : 팔로우 당하는 followed, 팔로우하는 following 유저가 존재한다.
        User followed = userRepository.save(User.builder()
                .name("followed").email("followed@test.com").role(Role.USER).build());
        User following = userRepository.save(User.builder()
                .name("following").email("following@test.com").role(Role.USER).build());

        // when : following 유저가 followed 유저를 팔로우한다.
        followService.follow(following.getEmail(),followed.getId());
        // @TransactionalEventListener의 AFTER_COMMIT을 위한 end 처리
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // then : sender가 following, receiver가 follower인 follow 알림이 발송된다.
        ArgumentCaptor<NotificationMessage> argumentCaptor =
                ArgumentCaptor.forClass(NotificationMessage.class);
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
            // Awaitility를 이용한 비동기 테스트
            verify(notificationMessageSender)
                    .sendNotificationMessage(argumentCaptor.capture());
        });
        NotificationMessage sent = argumentCaptor.getValue();
        assertEquals(NotificationType.FOLLOW, sent.getNotificationType());
        assertEquals(followed.getId(), sent.getReceiverId());
        assertEquals(following.getId(), sent.getSenderId());
    }
}
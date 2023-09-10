package com.recipetory.recipe.infrastructure;

import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeInfo;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.infrastructure.UserJpaRepository;
import com.recipetory.utils.es.KafkaDocumentMessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@AutoConfigureTestDatabase
public class RecipeDocumentTest {
    @MockBean
    private KafkaDocumentMessageSender documentMessageSender;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeJpaRepository recipeJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    public void setUp() {
        recipeJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("recipe 엔티티를 생성하면 document 생성 메세지가 발송된다.")
    public void createRecipeDocumentTest() {
        // given, when : test 유저와 test 레시피를 저장한다.
        User author = userRepository
                .save(User.builder()
                        .name("test").role(Role.USER).email("test@test.com")
                        .build());
        Recipe recipe = Recipe.builder()
                .title("test").tags(new ArrayList<>())
                .recipeStatistics(new RecipeStatistics()).recipeInfo(new RecipeInfo())
                .steps(new ArrayList<>())
                .build();
        recipeService.createRecipe(recipe, new ArrayList<>(), author.getEmail());
        // @TransactionalEventListener의 AFTER_COMMIT을 위한 end 처리
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // then : save된 entity와 같은 id의 document 저장 메세지가 발송된다.
        ArgumentCaptor<RecipeDocument> argumentCaptor =
                ArgumentCaptor.forClass(RecipeDocument.class);
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
            // Awaitility를 이용한 비동기 테스트
            verify(documentMessageSender).sendRecipeDocument(argumentCaptor.capture());
        });

        RecipeDocument sent = argumentCaptor.getValue();
        assertEquals(recipe.getId(), sent.getId());
        assertAll(() -> assertEquals(recipe.getAuthor().getId(),sent.getAuthorId()),
                () -> assertEquals(recipe.getTitle(),sent.getTitle()));
    }

    @Test
    @Transactional
    @DisplayName("recipe 엔티티를 삭제하면 document 생성 메세지가 발송된다.")
    public void deleteRecipeDocumentTest() {
        // given : test 유저와 test 레시피를 저장한다.
        User author = userRepository
                .save(User.builder()
                        .name("test").role(Role.USER).email("test@test.com")
                        .build());
        Recipe recipe = Recipe.builder()
                .title("test").tags(new ArrayList<>())
                .recipeStatistics(new RecipeStatistics()).recipeInfo(new RecipeInfo())
                .steps(new ArrayList<>())
                .build();
        recipeService.createRecipe(recipe, new ArrayList<>(), author.getEmail());

        // when : 저장된 recipe entity를 삭제한다.
        recipeService.deleteRecipeById(recipe.getId(),author.getEmail());
        // @TransactionalEventListener의 AFTER_COMMIT을 위한 end 처리
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        // then : delete된 entity와 같은 id의 document 삭제 메세지가 발송된다.
        ArgumentCaptor<Long> argumentCaptor =
                ArgumentCaptor.forClass(Long.class);
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
            // Awaitility를 이용한 비동기 테스트
            verify(documentMessageSender).sendDeleteRecipeDocument(argumentCaptor.capture());
        });

        Long sent = argumentCaptor.getValue();
        assertEquals(recipe.getId(), sent);
    }
}

package com.recipetory.reply.application;

import com.recipetory.TestRepositoryConfig;
import com.recipetory.TestServiceConfig;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.reply.domain.comment.CommentRepository;
import com.recipetory.reply.domain.exception.ReplyNotFoundException;
import com.recipetory.reply.presentation.comment.dto.CreateCommentDto;
import com.recipetory.reply.presentation.comment.dto.UpdateCommentDto;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TestRepositoryConfig.class, TestServiceConfig.class})
public class CommentServiceTest {
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    User user;
    Recipe recipe;
    String userEmail;

    @BeforeEach
    public void setUp() {
        commentService = new CommentService(
                commentRepository, recipeService, userService);

        // userEmail을 가진 유저와, 해당 유저가 작성한 레시피를 저장한다.
        userEmail = "user@test.com";
        user = userRepository.save(
                User.builder()
                        .name("user1").email(userEmail).role(Role.USER)
                        .build());
        recipe = recipeRepository.save(
                Recipe.builder()
                        .title("recipe1").recipeStatistics(new RecipeStatistics()).author(user)
                        .build());
    }

    @Test
    @DisplayName("작성한 댓글은 조회될 수 있다.")
    public void createCommentTest() {
        // given : createComment()를 통해 댓글을 생성한다.
        CreateCommentDto request = getCreateRequest();
        Comment created = commentService.createComment(userEmail, request);

        // when, then : comment Id, recipeId, userId로 댓글이 조회된다.
        assertEquals(created, commentService.getCommentById(created.getId()));
        assertTrue(commentService.getCommentByRecipeId(recipe.getId()).contains(created));
        assertTrue(commentService.getCommentByUserId(user.getId()).contains(created));
    }

    @Test
    @DisplayName("작성된 댓글 수가 레시피 정보에 반영된다.")
    public void commentCountTest() {
        // given, when : commentCount 만큼 레시피에 댓글을 작성한다.
        int commentCount = 10;
        for (int i = 0; i < commentCount; i++) {
            CreateCommentDto request = getCreateRequest();
            commentService.createComment(userEmail, request);
        }

        // then : recipe.commentCount == commentCount
        assertEquals(
                commentCount, recipe.getRecipeStatistics().getCommentCount());
    }

    @Test
    @DisplayName("댓글 작성자가 아닌 작성자는 댓글을 수정하거나 삭제할 수 없다.")
    public void invalidUserCommentTest() {
        // given 1 : user 유저가 댓글을 작성한다.
        CreateCommentDto createRequest = getCreateRequest();
        Comment created = commentService.createComment(userEmail, createRequest);

        // given 뷰2 : 댓글 작성자가 아닌 another 유저가 존재한다.
        User another = userRepository.save(User.builder()
                .email("another@test.com").name("another").role(Role.USER).build());

        // when, then : another 유저는 댓글 수정이나 삭제가 불가하다.
        assertThrows(NotOwnerException.class, () ->
                commentService.updateComment(another.getEmail(), created.getId(), new UpdateCommentDto("new content")));
        assertThrows(NotOwnerException.class, () ->
                commentService.deleteComment(another.getEmail(), created.getId()));
    }

    @Test
    @DisplayName("작성자는 댓글 본문을 수정할 수 있다.")
    public void updateCommentTest() {
        // given : 댓글을 작성한다.
        CreateCommentDto createRequest = getCreateRequest();
        Comment created = commentService.createComment(userEmail, createRequest);

        // when : 댓글 content가 newContent로 수정된다.
        String newContent = "modified comment";
        UpdateCommentDto updateRequest = new UpdateCommentDto(
                newContent);
        Comment updated = commentService.updateComment(userEmail, created.getId(), updateRequest);

        // then : 댓글에 대한 content가 newContent가 되었다.
        assertEquals(created, updated);
        assertEquals(newContent, updated.getContent());
    }

    @Test
    @DisplayName("댓글 작성자는 댓글 삭제가 가능하다.")
    public void deleteCommentTest() {
        // given : 댓글을 작성한다.
        CreateCommentDto createRequest = getCreateRequest();
        Comment created = commentService.createComment(userEmail, createRequest);

        // when : 해당 댓글을 삭제한다.
        commentService.deleteComment(userEmail, created.getId());

        // then : 댓글 id로 조회하면 ReplyNotFoundException이 발생한다.
        assertThrows(ReplyNotFoundException.class,
                () -> commentService.getCommentById(created.getId()));
    }

    /**
     * 테스트에 사용할 댓글 생성 request dto를 반환한다.
     * @return {@link CreateCommentDto}
     */
    private CreateCommentDto getCreateRequest() {
        return new CreateCommentDto(
                recipe.getId(), "test comment");
    }
}

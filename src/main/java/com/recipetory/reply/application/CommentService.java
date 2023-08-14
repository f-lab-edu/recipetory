package com.recipetory.reply.application;

import com.recipetory.notification.domain.event.CommentEvent;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.reply.domain.comment.CommentRepository;
import com.recipetory.reply.presentation.comment.dto.CreateCommentDto;
import com.recipetory.reply.presentation.comment.dto.UpdateCommentDto;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.User;
import com.recipetory.utils.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final RecipeService recipeService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 레시피에 대한 댓글을 추가한다.
     * @param logInEmail 댓글 작성하는 유저 email
     * @param commentDto 생성하는 댓글 dto
     * @return 생성되어 DB에 저장된 댓글
     */
    @Transactional
    public Comment createComment(String logInEmail,
                                 CreateCommentDto commentDto) {

        User author = userService.getUserByEmail(logInEmail);
        Recipe recipe = recipeService.getRecipeById(commentDto.getRecipeId());

        Comment created = commentRepository.save(
                commentDto.toEntity(author,recipe));
        updateCommentCount(recipe);

        eventPublisher.publishEvent(new CommentEvent(created));

        return created;
    }

    /**
     * 특정 id를 가진 레시피에 달린 댓글을 조회한다.
     * @param recipeId 레시피 id
     * @return 레시피에 달린 댓글 List
     */
    @Transactional(readOnly = true)
    public List<Comment> getCommentByRecipeId(Long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return commentRepository.findByRecipe(recipe);
    }

    /**
     * 특정 유저가 작성한 댓글을 조회한다.
     * @param userId 댓글 id
     * @return 유저가 작성한 댓글 List
     */
    @Transactional(readOnly = true)
    public List<Comment> getCommentByUserId(Long userId) {
        User author = userService.getUserById(userId);
        return commentRepository.findByAuthor(author);
    }

    /**
     * 특정 id를 가진 댓글 내용을 업데이트한다.
     * @param logInEmail 리뷰 작성자 email
     * @param commentId 수정하고자 하는 리뷰 id
     * @param dto update comment dto
     * @return 수정된 댓글
     */
    @Transactional
    public Comment updateComment(String logInEmail,
                                 Long commentId,
                                 UpdateCommentDto commentDto) {
        Comment foundComment = getCommentById(commentId);
        User currentUser = userService.getUserByEmail(logInEmail);

        foundComment.verifyAuthor(currentUser);
        foundComment.updateContent(commentDto);
        return foundComment;
    }

    /**
     * 해당하는 id의 댓글 삭제한다.
     * @param logInEmail 댓글 주인 유저의 이메일
     * @param commentId 댓글 id
     */
    @Transactional
    public void deleteComment(String logInEmail,
                              Long commentId) {
        Comment foundComment = getCommentById(commentId);
        User currentUser = userService.getUserByEmail(logInEmail);

        foundComment.verifyAuthor(currentUser);
        commentRepository.delete(foundComment);

        updateCommentCount(foundComment.getRecipe());
    }

    /**
     * 특정 id를 가진 리뷰를 조회한다.
     * @param commentId id of review
     * @return found review
     */
    @Transactional(readOnly = true)
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment",String.valueOf(commentId)));
    }

    /**
     * Comment 관련한 Recipe statistics 정보를 업데이트한다.
     * 댓글 조회 혹은 레시피 조회 시에는 호출되지 않는다.
     * @param recipe 업데이트되는 레시피
     */
    @Transactional
    public void updateCommentCount(Recipe recipe) {
        int commentCount = commentRepository.countByRecipe(recipe);
        recipe.getRecipeStatistics().updateCommentCount(commentCount);
    }
}

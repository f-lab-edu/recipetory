package com.recipetory.reply.presentation.comment;

import com.recipetory.config.auth.argumentresolver.LogInUser;
import com.recipetory.config.auth.dto.SessionUser;
import com.recipetory.reply.application.CommentService;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.reply.presentation.comment.dto.CommentDto;
import com.recipetory.reply.presentation.comment.dto.CommentListDto;
import com.recipetory.reply.presentation.comment.dto.CreateCommentDto;
import com.recipetory.reply.presentation.comment.dto.UpdateCommentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글을 생성한다.
     * @param request 생성하려는 댓글 {@link CreateCommentDto}
     * @param logInUser 현재 로그인한 user id
     * @return 생성된 댓글 response body
     */
    @PostMapping("/comments")
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CreateCommentDto request,
            @LogInUser SessionUser logInUser) {

        Comment created = commentService.createComment(
                logInUser.getEmail(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommentDto.fromEntity(created));
    }

    /**
     * 특정 id의 댓글을 조회한다.
     * 존재하지 않는 댓글엔 ReplyNotFoundException이 발생한다.
     * @param commentId commentId
     * @return found comment
     */
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> readComment(
            @PathVariable("commentId") Long commentId) {

        Comment found = commentService.getCommentById(commentId);
        return ResponseEntity.ok(CommentDto.fromEntity(found));
    }

    /**
     * 특정 유저가 작성한 댓글들을 조회한다.
     * @param userId {@link PathVariable}로 주어진 유저 id
     * @return 유저가 작성한 댓글 목록 response body
     */
    @GetMapping("/{userId}/comments")
    public ResponseEntity<CommentListDto> getCommentsOfUser(
            @PathVariable("userId") Long userId) {

        List<Comment> comments = commentService.getCommentByUserId(userId);
        CommentListDto commentDtos = CommentListDto.fromEntityList(comments);
        return ResponseEntity.ok(commentDtos);
    }

    /**
     * 특정 레시피에 작성된 댓글 조회한다.
     * @param recipeId {@link PathVariable}로 주어진 레시피 id
     * @return 레시피에 작성된 댓글 목록 response body
     */
    @GetMapping("/recipe/{recipeId}/comments")
    public ResponseEntity<CommentListDto> getCommentList(
            @PathVariable("recipeId") Long recipeId) {

        List<Comment> comments = commentService.getCommentByRecipeId(recipeId);
        CommentListDto commentDtos = CommentListDto.fromEntityList(comments);
        return ResponseEntity.ok(commentDtos);
    }

    /**
     * 댓글을 수정한다.
     * @param commentId 수정하고자 하는 댓글 id
     * @param logInUser 현재 로그인된 {@link SessionUser}. 댓글 작성자와 같아야한다.
     * @param request request dto
     * @return 수정된 댓글 response body
     */
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("commentId") Long commentId,
            @LogInUser SessionUser logInUser,
            @Valid @RequestBody UpdateCommentDto request) {

        Comment updated = commentService.updateComment(
                logInUser.getEmail(), commentId, request);
        return ResponseEntity.ok(CommentDto.fromEntity(updated));
    }

    /**
     * 댓글을 삭제한다.
     * @param commentId 삭제하고자 하는 댓글 id
     * @param logInUser 현재 로그인된 {@link SessionUser}. 댓글의 author와 같아야한다.
     * @return empty body
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("commentId") Long commentId,
            @LogInUser SessionUser logInUser) {

        commentService.deleteComment(logInUser.getEmail(), commentId);
        return ResponseEntity.ok().build();
    }
}

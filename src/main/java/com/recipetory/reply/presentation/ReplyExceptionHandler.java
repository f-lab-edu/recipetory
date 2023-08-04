package com.recipetory.reply.presentation;

import com.recipetory.reply.domain.exception.CannotReviewException;
import com.recipetory.reply.domain.exception.ReplyNotFoundException;
import com.recipetory.utils.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ReplyExceptionHandler {
    @ExceptionHandler(ReplyNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(
            ReplyNotFoundException e) {
        log.warn("ReplyNotFoundException");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse("해당하는 리뷰 혹은 댓글은 존재하지 않습니다."));
    }

    @ExceptionHandler(CannotReviewException.class)
    public ResponseEntity<ExceptionResponse> handleCannotReviewException(
            CannotReviewException e) {
        log.warn("CannotReviewException - recipe {} : author {}",
                e.getRecipeId(), e.getAuthorId(), e);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse("레시피 주인은 리뷰를 추가할 수 없습니다."));
    }
}

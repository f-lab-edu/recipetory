package com.recipetory.bookmark.presentation;

import com.recipetory.bookmark.domain.exception.CannotBookMarkException;
import com.recipetory.utils.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BookMarkExceptionHandler {
    @ExceptionHandler(CannotBookMarkException.class)
    public ResponseEntity<ExceptionResponse> handleCannotBookMarkException(
            CannotBookMarkException e) {
        log.info(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse("사용자 본인의 레시피는 북마크할 수 없습니다."));
    }
}

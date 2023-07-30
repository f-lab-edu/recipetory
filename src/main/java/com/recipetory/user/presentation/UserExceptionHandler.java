package com.recipetory.user.presentation;

import com.recipetory.user.domain.exception.InvalidUserRoleException;
import com.recipetory.user.domain.exception.UserNotFoundException;
import com.recipetory.utils.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(
            UserNotFoundException e) {

        log.info("UserNotFoundException!! - " +
                "key type: {}, key: {}",
                e.getUserKeyType(),e.getKey());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse("존재하지 않는 유저에 대한 요청입니다."));
    }

    @ExceptionHandler(InvalidUserRoleException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidUserRoleException(
            InvalidUserRoleException e) {
        
        log.info("InvalidUserRoleException!! - " +
                "userId: {}, currentRole: {}, requiredRole: {}",
                e.getUserId(), e.getCurrentRole(), e.getRequiredRole());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse("권한이 없는 요청입니다."));
    }
}

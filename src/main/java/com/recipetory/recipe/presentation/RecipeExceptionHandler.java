package com.recipetory.recipe.presentation;

import com.recipetory.recipe.domain.exception.RecipeNotFoundException;
import com.recipetory.utils.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RecipeExceptionHandler {
    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRecipeNotFoundException(
            RecipeNotFoundException e) {
        log.info("RecipeNotFoundException for key {} 발생", e.getKey());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse("존재하지 않는 레시피입니다."));
    }
}

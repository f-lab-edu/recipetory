package com.recipetory.bookmark.presentation;

import com.recipetory.bookmark.application.BookMarkService;
import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.bookmark.presentation.dto.BookMarkDto;
import com.recipetory.bookmark.presentation.dto.BookMarkListDto;
import com.recipetory.config.auth.argumentresolver.LogInUser;
import com.recipetory.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookMarkController {
    private final BookMarkService bookMarkService;

    /**
     * path variable의 유저가 북마크를 조회한다.
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/bookmarks")
    public ResponseEntity<BookMarkListDto> findBookMarksOfUser(
            @PathVariable("userId") Long userId) {
        List<BookMark> foundBookMarks = bookMarkService
                .findBookMarkByUserId(userId);

        return ResponseEntity.ok(
                BookMarkListDto.fromEntityList(foundBookMarks));
    }

    @GetMapping("/bookmarks/{recipeId}")
    public ResponseEntity<BookMarkListDto> findBookMarksOfRecipe(
            @PathVariable("recipeId") Long recipeId) {
        List<BookMark> foundBookMarks = bookMarkService.findBookMarkByRecipeId(recipeId);
        BookMarkListDto bookMarkListDto = BookMarkListDto.fromEntityList(foundBookMarks);

        return ResponseEntity.ok(bookMarkListDto);
    }

    @PostMapping("/bookmarks/{recipeId}")
    public ResponseEntity<BookMarkDto> addBookMark(
            @LogInUser SessionUser logInUser,
            @PathVariable("recipeId") Long recipeId) {
        BookMark saved = bookMarkService.addBookMark(
                logInUser.getEmail(), recipeId);

        return ResponseEntity.ok(BookMarkDto.fromEntity(saved));
    }

    @DeleteMapping("/bookmarks/{recipeId}")
    public ResponseEntity<Void> deleteBookMark(
            @LogInUser SessionUser logInUser,
            @PathVariable("recipeId") Long recipeId) {
        bookMarkService.deleteBookMark(logInUser.getEmail(),recipeId);
        return ResponseEntity.noContent().build();
    }
}

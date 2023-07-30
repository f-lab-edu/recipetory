package com.recipetory.user.presentation;

import com.recipetory.bookmark.application.BookMarkService;
import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.bookmark.presentation.dto.BookMarkListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final BookMarkService bookMarkService;

    @GetMapping("/{userId}/bookmark")
    public ResponseEntity<BookMarkListDto> findBookMarksOfUser(
            @PathVariable("userId") Long userId) {
        List<BookMark> foundBookMarks = bookMarkService
                .findBookMarkByUserId(userId);

        return ResponseEntity.ok(
                BookMarkListDto.fromEntityList(foundBookMarks));
    }
}

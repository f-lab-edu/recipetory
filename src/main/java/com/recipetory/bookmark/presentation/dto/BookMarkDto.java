package com.recipetory.bookmark.presentation.dto;

import com.recipetory.bookmark.domain.BookMark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BookMarkDto {
    private Long bookMarkerId;
    private Long recipeId;

    public static BookMarkDto fromEntity(BookMark bookMark) {
        return BookMarkDto.builder()
                .bookMarkerId(bookMark.getBookMarker().getId())
                .recipeId(bookMark.getRecipe().getId())
                .build();
    }
}

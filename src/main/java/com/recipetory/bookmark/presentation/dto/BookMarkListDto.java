package com.recipetory.bookmark.presentation.dto;

import com.recipetory.bookmark.domain.BookMark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BookMarkListDto {
    private List<BookMarkDto> bookMarks = new ArrayList<>();

    public static BookMarkListDto fromEntityList(List<BookMark> bookMarks) {
        return BookMarkListDto.builder()
                .bookMarks(bookMarks.stream()
                        .map(BookMarkDto::fromEntity)
                        .toList())
                .build();
    }
}

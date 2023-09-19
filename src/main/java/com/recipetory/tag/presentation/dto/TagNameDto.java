package com.recipetory.tag.presentation.dto;

import com.recipetory.tag.domain.TagName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TagNameDto {
    private String name;
    private String description;

    public static List<TagNameDto> getAllTags() {
        return Arrays.stream(TagName.values()).map(tagName ->
                TagNameDto.builder()
                        .name(tagName.name()).description(tagName.getKorean())
                        .build()).toList();
    }
}

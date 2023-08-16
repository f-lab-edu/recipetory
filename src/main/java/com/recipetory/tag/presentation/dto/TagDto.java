package com.recipetory.tag.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.recipetory.tag.domain.Tag;
import com.recipetory.tag.domain.TagName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDto {
    private Long tagId;

    @NotNull
    private TagName tagName;

    public Tag toEntity() {
        return Tag.builder()
                .tagName(tagName)
                .build();
    }

    public static TagDto fromEntity(Tag tag) {
        return TagDto.builder()
                .tagId(tag.getId())
                .tagName(tag.getTagName())
                .build();
    }
}

package com.recipetory.tag.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TagType {
    TYPE("음식 종류별"),
    METHOD("조리 방법별"),
    SITUATION("상황별");

    private final String description;
}

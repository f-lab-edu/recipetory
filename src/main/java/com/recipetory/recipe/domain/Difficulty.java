package com.recipetory.recipe.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Difficulty {
    EASY("쉬움"),
    NORMAL("보통"),
    HARD("어려움"),
    UNDEFINED("선택안함");

    private final String description;
}

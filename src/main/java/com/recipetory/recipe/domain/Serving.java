package com.recipetory.recipe.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Serving {
    ONE("1인분"),
    TWO("2인분"),
    THREE("3인분"),
    FOUR("4인분"),
    MORE("5인분"),
    UNDEFINED("선택안함");

    private final String description;
}

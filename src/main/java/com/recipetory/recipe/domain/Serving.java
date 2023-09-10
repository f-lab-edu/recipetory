package com.recipetory.recipe.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Serving {
    ONE("1인분"),
    TWO("2인분"),
    THREE("3인분"),
    FOUR("4인분"),
    MORE("5인분 이상"),
    UNDEFINED("선택안함");

    private final String description;

    public static Serving fromString(String input) {
        return Arrays.stream(Serving.values())
                .filter(serving -> serving.name()
                        .equalsIgnoreCase(input))
                .findAny().orElse(UNDEFINED);
    }
}

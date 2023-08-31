package com.recipetory.recipe.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum Difficulty {
    EASY("쉬움"),
    NORMAL("보통"),
    HARD("어려움"),
    UNDEFINED("선택안함");

    private final String description;

    public static Difficulty fromString(String input) {
        return Arrays.stream(Difficulty.values())
                .filter(difficulty -> difficulty.name()
                        .equalsIgnoreCase(input))
                .findAny().orElse(UNDEFINED);
    }
}

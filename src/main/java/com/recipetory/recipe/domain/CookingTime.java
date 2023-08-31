package com.recipetory.recipe.domain;

import com.recipetory.tag.domain.TagName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum CookingTime {
    LESS_THAN_10("10분 이내"),
    LESS_THAN_20("20분 이내"),
    LESS_THAN_30("30분 이내"),
    LESS_THAN_60("1시간 이내"),
    LESS_THAN_90("1시간 30분 이내"),
    LESS_THAN_120("2시간 이내"),
    MORE_THAN_120("2시간 이상"),
    UNDEFINED("선택안함");

    private final String description;

    public static CookingTime fromString(String input) {
        return Arrays.stream(CookingTime.values())
                .filter(cookingTime -> cookingTime.name()
                        .equalsIgnoreCase(input))
                .findAny().orElse(UNDEFINED);
    }
}

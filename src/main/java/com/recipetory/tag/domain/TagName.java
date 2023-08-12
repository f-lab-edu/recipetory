package com.recipetory.tag.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TagName {
    MAIN("주요리",TagType.TYPE),
    SIDE("반찬",TagType.TYPE),
    RICE("밥",TagType.TYPE),
    SOUP("국",TagType.TYPE),
    SALTED("젓갈/장류",TagType.TYPE),
    SAUCE("소스류",TagType.TYPE),
    NOODLE("면",TagType.TYPE),
    SALAD("샐러드",TagType.TYPE),
    FUSION("퓨전",TagType.TYPE),
    BREAD("빵",TagType.TYPE),
    DESSERT("디저트", TagType.TYPE),
    DRINK("음료",TagType.TYPE),
    CANDY("과자",TagType.TYPE),

    NORMAL("일상",TagType.SITUATION),
    SIMPLE("간단한",TagType.SITUATION),
    LUNCHBOX("도시락",TagType.SITUATION),
    MIDNIGHT("야식",TagType.SITUATION),
    DIET("다이어트",TagType.SITUATION),
    SNACK("간식",TagType.SITUATION),
    ENTERTAIN("손님 접대",TagType.SITUATION),
    HEALTH("헬스/건강식",TagType.SITUATION),

    FRY("튀김",TagType.METHOD),
    BOIL("삶음",TagType.METHOD),
    STEAM("찜",TagType.METHOD),
    ROAST("구이",TagType.METHOD),
    BIBIM("비빔/무침",TagType.METHOD),
    JORIM("조림",TagType.METHOD),
    RAW("회/날음식",TagType.METHOD);

    private final String korean;
    private final TagType tagType;
}

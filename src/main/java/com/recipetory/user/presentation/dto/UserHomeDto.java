package com.recipetory.user.presentation.dto;

import com.recipetory.recipe.presentation.dto.RecipeTitleDto;
import com.recipetory.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserHomeDto {
    private String name;
    private String profileImage = "";
    private List<RecipeTitleDto> recipes = new ArrayList<>();

    public static UserHomeDto fromEntity(User user) {
        List<RecipeTitleDto> recipeTitleDtos = user.getRecipes()
                .stream().map(RecipeTitleDto::fromEntity).toList();

        return UserHomeDto.builder()
                .name(user.getName())
                .profileImage(user.getImageUrl())
                .recipes(recipeTitleDtos)
                .build();
    }
}

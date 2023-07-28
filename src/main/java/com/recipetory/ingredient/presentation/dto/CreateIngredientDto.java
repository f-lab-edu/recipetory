package com.recipetory.ingredient.presentation.dto;

import com.recipetory.ingredient.domain.Ingredient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class CreateIngredientDto {
    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotNull
        @NotBlank
        private String name;

        @Length(max = 500)
        private String description = "";

        public Ingredient toEntity() {
            return Ingredient.builder()
                    .name(name)
                    .description(description)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        @NotNull
        @NotBlank
        private String name;

        @Builder.Default
        private String description = "";

        public static CreateIngredientDto.Response fromEntity(
                Ingredient ingredient) {
            return CreateIngredientDto.Response.builder()
                    .name(ingredient.getName())
                    .description(ingredient.getDescription())
                    .build();
        }
    }
}

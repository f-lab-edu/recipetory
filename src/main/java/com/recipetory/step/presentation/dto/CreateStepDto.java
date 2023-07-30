package com.recipetory.step.presentation.dto;

import com.recipetory.step.domain.Step;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class CreateStepDto {
    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotNull
        @Min(value = 1)
        private int stepNumber;

        @NotNull
        @NotBlank
        @Length(min = 1, max = 2000)
        private String description = "";

        public Step toEntity() {
            return Step.builder()
                    .stepNumber(stepNumber)
                    .description(description)
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private int stepNumber;
        private String description;

        public static CreateStepDto.Response fromEntity(Step step) {
            return Response.builder()
                    .stepNumber(step.getStepNumber())
                    .description(step.getDescription())
                    .build();
        }
    }
}

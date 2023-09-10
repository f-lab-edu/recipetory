package com.recipetory.step.presentation.dto;

import com.recipetory.recipe.domain.document.StepDocument;
import com.recipetory.step.domain.Step;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class StepDto {
    @NotNull
    @Min(value = 1)
    private int stepNumber;

    @NotEmpty
    @Length(min = 1, max = 2000)
    private String description;

    public static StepDto fromEntity(Step step) {
        return StepDto.builder()
                .stepNumber(step.getStepNumber())
                .description(step.getDescription())
                .build();
    }

    public Step toEntity() {
        return Step.builder()
                .stepNumber(stepNumber).description(description)
                .build();
    }

    public static StepDto fromDocument(StepDocument stepDocument) {
        return StepDto.builder()
                .stepNumber(stepDocument.getStepNumber())
                .description(stepDocument.getDescription())
                .build();
    }
}

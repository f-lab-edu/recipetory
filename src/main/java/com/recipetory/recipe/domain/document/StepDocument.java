package com.recipetory.recipe.domain.document;

import com.recipetory.step.domain.Step;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * recipe document의 nested field
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StepDocument {
    private int stepNumber;
    private String description;

    private static StepDocument fromEntity(Step step) {
        return StepDocument.builder()
                .stepNumber(step.getStepNumber()).description(step.getDescription())
                .build();
    }
    static List<StepDocument> fromEntityList(List<Step> steps) {
        return steps.stream().map(StepDocument::fromEntity).toList();
    }
}

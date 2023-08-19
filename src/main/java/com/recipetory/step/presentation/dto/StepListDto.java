package com.recipetory.step.presentation.dto;

import com.recipetory.step.domain.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StepListDto {
    private List<StepDto> steps = new ArrayList<>();

    public static StepListDto fromEntityList(List<Step> steps) {
        List<StepDto> stepDtos = steps.stream()
                .map(StepDto::fromEntity).toList();
        return new StepListDto(stepDtos);
    }
}

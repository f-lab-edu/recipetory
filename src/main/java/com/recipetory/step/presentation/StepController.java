package com.recipetory.step.presentation;

import com.recipetory.step.application.StepService;
import com.recipetory.step.presentation.dto.StepListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/steps")
public class StepController {
    private final StepService stepService;

    /**
     * path variable에 해당하는 레시피의 단계 list를 반환한다.
     * @param recipeId path varible long
     * @return
     */
    @GetMapping("/{recipeId}")
    public ResponseEntity<StepListDto> getStepsByRecipeId(
            @PathVariable("recipeId") Long recipeId
    ) {
        StepListDto found = stepService.getStepsByRecipeId(recipeId);
        return ResponseEntity.ok(found);
    }
}

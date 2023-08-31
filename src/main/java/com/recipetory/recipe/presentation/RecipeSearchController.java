package com.recipetory.recipe.presentation;

import com.recipetory.recipe.application.RecipeSearchService;
import com.recipetory.recipe.presentation.dto.RecipeListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RecipeSearchController {
    private final RecipeSearchService recipeSearchService;

    /**
     * path variable의 userId가 작성한 recipe를 반환한다.
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/recipes")
    public ResponseEntity<RecipeListDto> findByUserId(
            @PathVariable("userId") Long userId
    ) {
        RecipeListDto found = recipeSearchService.findByUserId(userId);
        return ResponseEntity.ok(found);
    }

    /**
     * title과 recipeInfo 조건에 맞는 레시피를 검색한다.
     * @param title
     * @param cookingTime
     * @param difficulty
     * @param serving
     * @return
     */
    @GetMapping("/recipes")
    public ResponseEntity<RecipeListDto> searchRecipe(
            @RequestParam(name = "q", defaultValue = "UNDEFINED") String title,
            @RequestParam(name = "t", defaultValue = "UNDEFINED") String cookingTime,
            @RequestParam(name = "d", defaultValue = "UNDEFINED") String difficulty,
            @RequestParam(name = "s", defaultValue = "UNDEFINED") String serving
    ) {
        RecipeListDto found = recipeSearchService.findRecipeByRecipeInfo(
                title.trim(), cookingTime, difficulty, serving);
        return ResponseEntity.ok(found);
    }

    /**
     * query parameter로 들어온 tag를 전부 포함하는 레시피를 검색한다.
     * @param tags
     * @return
     */
    @GetMapping("/recipes/tags")
    public ResponseEntity<RecipeListDto> searchRecipeByTags(
            @RequestParam(name = "t", defaultValue = "") List<String> tags
    ) {
        RecipeListDto found = recipeSearchService.findByTags(tags);
        return ResponseEntity.ok(found);
    }
}

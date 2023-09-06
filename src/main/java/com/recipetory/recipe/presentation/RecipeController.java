package com.recipetory.recipe.presentation;

import com.recipetory.config.auth.argumentresolver.LogInUser;
import com.recipetory.config.auth.dto.SessionUser;
import com.recipetory.ingredient.presentation.dto.RecipeIngredientDto;
import com.recipetory.recipe.application.RecipeSearchService;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.presentation.dto.CreateRecipeDto;
import com.recipetory.recipe.presentation.dto.RecipeDto;
import com.recipetory.recipe.presentation.dto.RecipeListDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeSearchService recipeSearchService;

    /**
     * request body로 요청된 recipe를 생성한다.
     * @param request
     * @param logInUser
     * @return
     */
    @PostMapping("/recipes")
    public ResponseEntity<CreateRecipeDto.Response> postRecipe(
            @RequestBody @Valid CreateRecipeDto.Request request,
            @LogInUser SessionUser logInUser) {
        Recipe requestRecipe = request.toEntity();
        List<RecipeIngredientDto> requestIngredients = request.getIngredients();

        Recipe created = recipeService.createRecipe(
                requestRecipe,
                requestIngredients,
                logInUser.getEmail());

        return ResponseEntity.ok(
                CreateRecipeDto.Response.fromEntity(created));
    }

    /**
     * path variable에 해당하는 recipe id를 가진 레시피를 조회하고 반환한다.
     * @param recipeId
     * @return
     */
    @GetMapping("/recipes/{recipeId}")
    public ResponseEntity<RecipeDto> findByRecipeId(
            @PathVariable("recipeId") Long recipeId) {

        RecipeDto found = recipeService.getCompleteRecipe(recipeId);
        return ResponseEntity.ok(found);
    }

    /**
     * path variable에 해당하는 id를 가진 레시피를 삭제한다.
     * @param recipeId path variable
     * @return
     */
    @DeleteMapping("/recipes/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(
            @PathVariable("recipeId") Long recipeId,
            @LogInUser SessionUser logInUser
    ) {
        recipeService.deleteRecipeById(recipeId,logInUser.getEmail());
        return ResponseEntity.ok().build();
    }
}

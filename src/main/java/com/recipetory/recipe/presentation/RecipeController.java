package com.recipetory.recipe.presentation;

import com.recipetory.config.auth.argumentresolver.LogInUser;
import com.recipetory.config.auth.dto.SessionUser;
import com.recipetory.ingredient.application.IngredientService;
import com.recipetory.ingredient.presentation.dto.IngredientDto;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.presentation.dto.CreateRecipeDto;
import com.recipetory.step.presentation.dto.CreateStepDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
@Slf4j
public class RecipeController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<CreateRecipeDto.Response> postRecipe(
            @RequestBody @Valid CreateRecipeDto.Request request,
            @LogInUser SessionUser logInUser) {
        Recipe requestRecipe = request.toEntity();
        List<IngredientDto> requestIngredients = request.getIngredients();

        Recipe created = recipeService.createRecipe(
                requestRecipe,
                requestIngredients,
                logInUser.getEmail());

        CreateRecipeDto.Response response = CreateRecipeDto.Response.builder()
                        .title(created.getTitle())
                        .recipeInfo(created.getRecipeInfo())
                        .ingredients(created.getIngredients().stream()
                                .map(IngredientDto::fromEntity)
                                .toList())
                        .steps(created.getSteps().stream()
                                .map(CreateStepDto.Response::fromEntity)
                                .toList())
                .build();

        return ResponseEntity.ok(response);
    }
}

package com.recipetory.recipe.application;

import com.recipetory.ingredient.domain.IngredientRepository;
import com.recipetory.ingredient.domain.RecipeIngredientRepository;
import com.recipetory.ingredient.presentation.dto.RecipeIngredientDto;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.step.domain.Step;
import com.recipetory.step.domain.StepRepository;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.exception.InvalidUserRoleException;
import com.recipetory.utils.exception.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class RecipeServiceTest {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private StepRepository stepRepository;

    @Test
    @Transactional
    @DisplayName("GUEST 유저가 레시피를 포스트하면 InvalidUserRoleException이 발생한다.")
    public void testInvalidUserPost() {
        // given
        String testEmail = "test@test.com";
        User user = userRepository.save(User.builder()
                .name("GUEST USER").email(testEmail).role(Role.GUEST)
                .build());
        Recipe recipe = Recipe.builder()
                .title("TEST RECIPE")
                .build();

        Assertions.assertThrows(InvalidUserRoleException.class,() -> {
            recipeService.createRecipe(recipe, new ArrayList<>(), testEmail);
        });
    }

    @Test
    @DisplayName("존재하지 않는 유저 email에 대한 post는 EntityNotFoundException이 발생한다.")
    public void notFoundUserPost() {
        String testEmail = "notfound@test.com";
        Recipe recipe = Recipe.builder()
                .title("TEST RECIPE")
                .build();

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                recipeService.createRecipe(recipe,new ArrayList<>(),testEmail));
    }

    @Test
    @Transactional
    @DisplayName("createRecipe()를 통해 존재하지 않았던 ingredient가 생성된다.")
    public void testIngredientSaved() {
        // given
        String testEmail = "test@test.com";
        User user = userRepository.save(User.builder()
                .name("USER").email(testEmail).role(Role.USER)
                .build());
        Recipe recipe = Recipe.builder()
                .title("TEST RECIPE")
                .steps(new ArrayList<>())
                .recipeStatistics(new RecipeStatistics())
                .build();
        List<RecipeIngredientDto> recipeIngredientDtos = Arrays.asList(
                RecipeIngredientDto.builder().name("재료1").build(),
                RecipeIngredientDto.builder().name("재료2").build(),
                RecipeIngredientDto.builder().name("재료3").build()
        );

        // when : recipe create가 일어난다면,
        recipeService.createRecipe(recipe, recipeIngredientDtos,testEmail);

        // then : ingredient의 name을 가진 ingredient가 DB에 존재한다.
        recipeIngredientDtos.stream().map(RecipeIngredientDto::getName)
                .forEach(name -> {
                    Assertions.assertNotNull(
                            ingredientRepository.findByName(name));
                });
    }

    @Test
    @Transactional
    @DisplayName("Recipe 관련 연관 관계가 잘 setting되었다.")
    public void testRelations() {
        // given & when
        Recipe saved = saveTestRecipe();
        Recipe found = recipeRepository.findByTitle(saved.getTitle()).orElseThrow();

        // then-1 : step
        found.getSteps().forEach(step -> {
            Assertions.assertNotNull(step.getRecipe());
        });

        // then-2 : user
        Assertions.assertEquals("test@test.com", found.getAuthor().getEmail());

        // then-3 : ingredient
        Assertions.assertEquals(saved.getIngredients().size(),
                recipeIngredientRepository.findByRecipe(found).size());
    }

    private Recipe saveTestRecipe() {
        String testEmail = "test@test.com";
        User user = userRepository.save(User.builder()
                .name("USER").email(testEmail).role(Role.USER)
                .build());

        List<RecipeIngredientDto> recipeIngredientDtos = Arrays.asList(
                RecipeIngredientDto.builder().name("재료1").build(),
                RecipeIngredientDto.builder().name("재료2").build(),
                RecipeIngredientDto.builder().name("재료3").build());

        List<Step> steps = Arrays.asList(
                stepRepository.save(Step.builder().description("단계1").build()),
                stepRepository.save(Step.builder().description("단계2").build()),
                stepRepository.save(Step.builder().description("단계3").build()));

        Recipe recipe = Recipe.builder()
                .title("TEST RECIPE")
                .steps(steps)
                .recipeStatistics(new RecipeStatistics())
                .build();

        return recipeService.createRecipe(recipe, recipeIngredientDtos,testEmail);
    }
}

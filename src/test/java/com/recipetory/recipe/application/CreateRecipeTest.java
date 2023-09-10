package com.recipetory.recipe.application;

import com.recipetory.ingredient.application.IngredientService;
import com.recipetory.ingredient.application.TestIngredientRepository;
import com.recipetory.ingredient.domain.IngredientRepository;
import com.recipetory.ingredient.presentation.dto.RecipeIngredientDto;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.service.TestUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CreateRecipeTest {
    private RecipeService recipeService;
    private RecipeRepository recipeRepository = new TestRecipeRepository();
    private UserRepository userRepository = new TestUserRepository();

    private IngredientRepository ingredientRepository = new TestIngredientRepository();
    private IngredientService ingredientService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    public void setUp() {
        ingredientService = new IngredientService(ingredientRepository);
        recipeService = new RecipeService(
                recipeRepository,ingredientService,userRepository,eventPublisher);
    }

    @Test
    @Transactional
    @DisplayName("createRecipe()를 통해 존재하지 않았던 ingredient가 생성된다.")
    public void testIngredientSaved() {
        // given
        User user = userRepository.save(User.builder()
                .name("USER").email("test@test.com").role(Role.USER)
                .build());
        Recipe recipe = Recipe.builder()
                .title("TEST RECIPE")
                .steps(new ArrayList<>()).tags(new ArrayList<>()).recipeStatistics(new RecipeStatistics())
                .build();
        List<RecipeIngredientDto> recipeIngredientDtos = Arrays.asList(
                RecipeIngredientDto.builder().name("재료1").build(),
                RecipeIngredientDto.builder().name("재료2").build(),
                RecipeIngredientDto.builder().name("재료3").build()
        );

        // when : recipe create가 일어난다면,
        recipeService.createRecipe(recipe, recipeIngredientDtos, user.getEmail());

        // then : ingredient의 name을 가진 ingredient가 DB에 존재한다.
        recipeIngredientDtos.stream().map(RecipeIngredientDto::getName)
                .forEach(name -> {
                    assertNotNull(
                            ingredientRepository.findByName(name));
                });
    }
}

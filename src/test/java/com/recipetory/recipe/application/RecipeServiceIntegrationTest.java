package com.recipetory.recipe.application;

import com.recipetory.ingredient.domain.IngredientRepository;
import com.recipetory.ingredient.presentation.dto.RecipeIngredientDto;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
public class RecipeServiceIntegrationTest {
    @MockBean
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    @DisplayName("createRecipe()를 통해 존재하지 않았던 ingredient가 생성된다.")
    public void testIngredientSaved() {
        // given
        String testEmail = "test@test.com";
        User user = userRepository.save(User.builder()
                .name("USER").email(testEmail).role(Role.USER)
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
        recipeService.createRecipe(recipe, recipeIngredientDtos,testEmail);

        // then : ingredient의 name을 가진 ingredient가 DB에 존재한다.
        recipeIngredientDtos.stream().map(RecipeIngredientDto::getName)
                .forEach(name -> {
                    Assertions.assertNotNull(
                            ingredientRepository.findByName(name));
                });
    }
}

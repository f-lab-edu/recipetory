package com.recipetory.recipe.application;

import com.recipetory.ingredient.application.IngredientService;
import com.recipetory.ingredient.presentation.dto.RecipeIngredientDto;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.step.domain.Step;
import com.recipetory.tag.domain.Tag;
import com.recipetory.tag.domain.TagName;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.exception.InvalidUserRoleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceUnitTest {
    private RecipeService recipeService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IngredientService ingredientService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    public void setUp() {
        RecipeRepository testRecipeRepository = new TestRecipeRepository();
        recipeService = new RecipeService(
                testRecipeRepository,ingredientService,userRepository,eventPublisher);
    }

    @Test
    @DisplayName("GUEST 유저가 레시피를 포스트하면 InvalidUserRoleException이 발생한다.")
    public void testInvalidUserPost() {
        // given
        String testEmail = "test@test.com";
        User user = User.builder()
                .name("GUEST USER").email(testEmail).role(Role.GUEST)
                .build();
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(user));
        Recipe recipe = Recipe.builder()
                .title("TEST RECIPE")
                .build();

        assertThrows(InvalidUserRoleException.class,() -> {
            recipeService.createRecipe(recipe, new ArrayList<>(), testEmail);
        });
    }

    @Test
    @DisplayName("Recipe 관련 연관 관계가 잘 setting되었다.")
    public void testRelations() {
        // given & when
        Recipe saved = saveTestRecipe();
        Recipe found = recipeService.getRecipeById(saved.getId());

        // then-1 : step
        found.getSteps().forEach(step -> {
            assertEquals(found,step.getRecipe());
        });

        // then-2 : user
        assertEquals("test@test.com", found.getAuthor().getEmail());

        // then-3 : tag
        found.getTags().forEach(tag -> {
            assertEquals(tag.getRecipe(),found);
        });
    }

    private Recipe saveTestRecipe() {
        String testEmail = "test@test.com";
        User user = User.builder()
                .name("USER").email(testEmail).role(Role.USER)
                .build();
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(user));

        List<RecipeIngredientDto> recipeIngredientDtos = Arrays.asList(
                RecipeIngredientDto.builder().name("재료1").build(),
                RecipeIngredientDto.builder().name("재료2").build(),
                RecipeIngredientDto.builder().name("재료3").build());

        List<Step> steps = Arrays.asList(
                Step.builder().description("단계1").build(),
                Step.builder().description("단계2").build(),
                Step.builder().description("단계3").build());

        List<Tag> tags = Arrays.asList(
                Tag.builder().tagName(TagName.BOIL).build(),
                Tag.builder().tagName(TagName.DIET).build());

        Recipe recipe = Recipe.builder()
                .title("TEST RECIPE").author(user).recipeStatistics(new RecipeStatistics())
                .steps(steps).tags(tags).build();

        return recipeService.createRecipe(recipe, recipeIngredientDtos,testEmail);
    }
}

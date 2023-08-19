package com.recipetory.recipe.application;

import com.recipetory.ingredient.application.IngredientService;
import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.ingredient.presentation.dto.RecipeIngredientDto;
import com.recipetory.notification.domain.event.CreateRecipeEvent;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.recipe.presentation.dto.RecipeDto;
import com.recipetory.recipe.presentation.dto.RecipeListDto;
import com.recipetory.step.domain.Step;
import com.recipetory.tag.domain.Tag;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.utils.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Recipe createRecipe(Recipe recipe,
                               List<RecipeIngredientDto> ingredients,
                               String authorEmail) {
        // 1. validate user role
        User author = findUserByEmail(authorEmail);
        author.verifyUserHasRole(Role.USER);

        // 2. ingredient -> recipeIngredient
        List<RecipeIngredient> recipeIngredients =
                ingredientService.convertToRelationEntity(
                        recipe, ingredients);

        // 3. set basic relations
        recipe.setBasicRelations(author,recipeIngredients);
        recipe.getSteps().forEach(step -> step.setRecipe(recipe));
        recipe.getTags().forEach(tag -> tag.setRecipe(recipe));

        // 4. save(persist) -> cascade
        Recipe saved = recipeRepository.save(recipe);

        // 5. send notification (@TransactionalEventListener이므로 커밋 후에 실행됨)
        eventPublisher.publishEvent(new CreateRecipeEvent(saved));

        return saved;
    }

    /**
     * 인자로 들어온 recipeId에 해당하는 레시피의 전체 정보를 반환한다.
     * @param recipeId
     * @return
     */
    @Transactional
    public RecipeDto getCompleteRecipe(Long recipeId) {
        Recipe found = getRecipeById(recipeId);
        User author = found.getAuthor();
        List<Step> steps = found.getSteps();
        List<RecipeIngredient> ingredients = found.getIngredients();
        List<Tag> tags = found.getTags();

        RecipeDto recipeDto = RecipeDto.fromEntity(found);
        recipeDto.setRelations(author, steps, ingredients, tags);
        return recipeDto;
    }

    /**
     * 특정 id를 가진 레시피를 찾는다.
     * @param recipeId id of recipe
     * @return found recipe
     */
    @Transactional(readOnly = true)
    public Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe", String.valueOf(recipeId)));
    }

    /**
     * 인자로 들어온 문자열을 title에 포함한 레시피를 검색한다.
     * @param word search keyword
     * @return recipe list dto
     */
    @Transactional(readOnly = true)
    public RecipeListDto findRecipeByTitleContains(String word) {
        if (word.isEmpty()) {
            return RecipeListDto.fromEntityList(new ArrayList<>());
        }

        List<Recipe> found = recipeRepository.findByTitleContains(word);
        return RecipeListDto.fromEntityList(found);
    }

    /**
     * 메소드 인자 id에 해당하는 레시피를 삭제한다.
     * casecadeType = ALL이기 때문에, 레시피와 연관 관계를 가진 엔티티들은 전부 삭제된다.
     * id에 해당하는 레시피가 없을 경우, jpa의 기본 설정에 따라 요청이 무시된다.
     * @param recipeId
     */
    @Transactional
    public void deleteRecipeById(Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }


    @Transactional
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User",email));
    }
}

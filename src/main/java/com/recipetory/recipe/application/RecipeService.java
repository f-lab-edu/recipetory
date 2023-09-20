package com.recipetory.recipe.application;

import com.recipetory.ingredient.application.IngredientService;
import com.recipetory.ingredient.domain.RecipeIngredient;
import com.recipetory.ingredient.presentation.dto.RecipeIngredientDto;
import com.recipetory.notification.domain.event.CreateRecipeEvent;
import com.recipetory.notification.domain.event.DeleteRecipeEvent;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.recipe.presentation.dto.RecipeDto;
import com.recipetory.recipe.presentation.dto.RecipeListDto;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.exception.NotOwnerException;
import com.recipetory.utils.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User author = getUserByEmail(authorEmail);
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
        RecipeDocument found = recipeRepository.getDocumentById(recipeId);
        User author = userRepository.findById(found.getAuthorId()).orElseThrow(() ->
                new EntityNotFoundException("User", String.valueOf(found.getId())));

        RecipeDto dto = RecipeDto.fromDocument(found);
        dto.setAuthorRelation(author);
        return dto;
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
     * 메소드 인자 id에 해당하는 레시피를 삭제한다.
     * casecadeType = ALL이기 때문에, 레시피와 연관 관계를 가진 엔티티들은 전부 삭제된다.
     * id에 해당하는 레시피가 없을 경우, jpa의 기본 설정에 따라 요청이 무시된다.
     * @param recipeId
     */
    @Transactional
    public void deleteRecipeById(Long recipeId, String logInEmail) {
        // 레시피 작성자만 삭제 가능
        Recipe found = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe", String.valueOf(recipeId)));
        validateRecipeAuthor(found, getUserByEmail(logInEmail));

        recipeRepository.deleteById(recipeId);
        eventPublisher.publishEvent(new DeleteRecipeEvent(recipeId));
    }

    /**
     * 레시피의 작성자가 맞는지 확인한다.
     * @param recipe target recipe
     * @param author expected author
     * @throws NotOwnerException author 아닐 경우
     */
    @Transactional(readOnly = true)
    private void validateRecipeAuthor(Recipe recipe, User author) {
        if (!recipe.isSameAuthor(author)) {
            throw new NotOwnerException(author.getId(), recipe.getAuthor().getId(),
                    "Recipe", String.valueOf(recipe.getId()));
        }
    }

    /**
     * index에 제공할 추천 레시피를 반환한다.
     * @return featured(추천) recipe
     */
    @Cacheable(value = "getFeaturedRecipes", cacheManager = "redisCacheManager")
    @Transactional(readOnly = true)
    public RecipeListDto getFeaturedRecipes() {
        List<RecipeDocument> featured = recipeRepository.getFeatured();
        return RecipeListDto.fromDocumentList(featured);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User",email));
    }
}

package com.recipetory.recipe.application;

import com.recipetory.recipe.domain.*;
import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.recipe.presentation.dto.RecipeListDto;
import com.recipetory.tag.domain.TagName;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.utils.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeSearchService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    /**
     * 인자로 들어온 userId의 유저가 작성한 레시피를 반환한다.
     * @param userId
     * @return {@link RecipeListDto}
     */
    @Transactional(readOnly = true)
    public RecipeListDto findByUserId(Long userId) {
        User author = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User", String.valueOf(userId)));
        List<RecipeDocument> found = recipeRepository.findByAuthor(author);
        return RecipeListDto.fromDocumentList(found);
    }

    /**
     * 인자로 들어온 String tag를 모두 포함하는 레시피를 조회한다.
     * @param tags {@link String} tag
     * @return {@link RecipeListDto}
     */
    @Transactional(readOnly = true)
    public RecipeListDto findByTags(List<String> tags) {
        // string tags를 tagName enum 타입으로 변환
        List<TagName> tagNames = convertToTagName(tags);
        // tagName List 전부를 포함하는 recipe 검색
        List<RecipeDocument> found = recipeRepository
                .findByTagNames(tagNames);

        return RecipeListDto.fromDocumentList(found);
    }

    /**
     * 인자로 들어온 조건에 맞는 레시피를 검색한다.
     * @param title
     * @param cookingTime
     * @param difficulty
     * @param serving
     * @return
     */
    @Transactional(readOnly = true)
    public RecipeListDto findRecipeByRecipeInfo(
            String title, String cookingTime, String difficulty, String serving) {
        List<RecipeDocument> found = recipeRepository.findByRecipeInfo(
                title,
                CookingTime.fromString(cookingTime),
                Difficulty.fromString(difficulty),
                Serving.fromString(serving));
        return RecipeListDto.fromDocumentList(found);
    }

    private List<TagName> convertToTagName(List<String> tags) {
        List<TagName> tagNames = new ArrayList<>();
        tags.forEach(tag ->
            TagName.fromString(tag)
                    .ifPresent(tagNames::add));
        return tagNames;
    }
}

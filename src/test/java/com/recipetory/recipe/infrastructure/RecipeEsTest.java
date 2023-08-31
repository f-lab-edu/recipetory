package com.recipetory.recipe.infrastructure;

import com.recipetory.recipe.domain.*;
import com.recipetory.recipe.domain.document.RecipeDocument;
import com.recipetory.tag.domain.TagName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RecipeEsTest {
    @Autowired
    private RecipeDocumentRepository recipeDocumentRepository;
    @Autowired
    private RecipeDocumentQueryService recipeDocumentQueryService;

    @AfterEach
    public void setUp() {
        recipeDocumentRepository.deleteAll();
    }
    @Test
    @DisplayName("tag가 모두 들어있는 레시피를 검색한다.")
    public void tagsSearchTest() {
        // given : recipe1은 BOIL, DIET, BREAD 태그를
        //         recipe2는 BOIL, DIET 태그를 갖는다.
        RecipeDocument recipe1 = RecipeDocument.builder().title("검색되어야하는 레시피")
                .recipeStatistics(new RecipeStatistics())
                .id(1L)
                .authorId(1L).createdAt(LocalDateTime.now())
                .tags(Arrays.asList(TagName.BOIL,TagName.DIET,TagName.BREAD))
                .recipeInfo(new RecipeInfo())
                .build();
        RecipeDocument recipe2 = RecipeDocument.builder().title("검색되면 안되는 레시피")
                .recipeStatistics(new RecipeStatistics())
                .id(2L)
                .authorId(1L).createdAt(LocalDateTime.now())
                .tags(Arrays.asList(TagName.BOIL,TagName.DIET))
                .recipeInfo(new RecipeInfo())
                .build();
        recipeDocumentRepository.saveAll(Arrays.asList(recipe1, recipe2));

        // when : BOIL, DIET, BREAD 태그로 findByTagNames()를 호출하여 레시피를 조회한다.
        List<RecipeDocument> found = recipeDocumentQueryService
                .findByTagNames(Arrays.asList(TagName.BOIL,TagName.DIET,TagName.BREAD));
        List<Long> foundIds = found.stream().map(RecipeDocument::getId).toList();

        // then : 3개의 태그를 모두 갖는 recipe1만이 조회 결과에 포함된다.
        assertAll(
                () -> assertTrue(foundIds.contains(recipe1.getId())),
                () -> assertFalse(foundIds.contains(recipe2.getId())));
    }

    @Test
    @DisplayName("recipeInfo 검색 조건으로 검색이 가능하다.")
    public void recipeInfoSearchTest() {
        // given : 특정 title, recipeInfo값을 갖는 레시피가 존재한다.
        RecipeInfo recipeInfo = RecipeInfo.builder()
                .cookingTime(CookingTime.LESS_THAN_20)
                .difficulty(Difficulty.HARD)
                .serving(Serving.ONE).build();
        RecipeDocument recipe = RecipeDocument.builder().id(1L)
                .title("검색되어야하는 레시피").recipeInfo(recipeInfo).build();
        recipeDocumentRepository.save(recipe);

        // when : 저장된 레시피의 title, recipeInfo를 조건으로 findByRecipeInfo()를 호출하여
        //        레시피를 조회한다.
        List<RecipeDocument> found = recipeDocumentQueryService.findByRecipeInfo(
                "레시피",
                recipeInfo.getCookingTime(),
                recipeInfo.getDifficulty(),
                recipeInfo.getServing());
        List<Long> foundIds = found.stream().map(RecipeDocument::getId).toList();

        // then : 조회 결과에 해당 레시피가 포함된다.
        assertTrue(foundIds.contains(recipe.getId()));
    }

    @Test
    @DisplayName("recipeInfo에 UNDEFINED 항목은 검색 조건에서 제외된다.")
    public void recipeInfoUndefinedTest() {
        // given : recipeInfo 값이 다른 recipe1, recipe2가 존재한다.
        RecipeDocument recipe1 = RecipeDocument.builder().id(1L)
                .title("test1")
                .recipeInfo(RecipeInfo.builder()
                        .cookingTime(CookingTime.LESS_THAN_20).difficulty(Difficulty.HARD).serving(Serving.ONE)
                        .build()).build();
        RecipeDocument recipe2 = RecipeDocument.builder().id(2L)
                .title("test2")
                .recipeInfo(RecipeInfo.builder()
                        .cookingTime(CookingTime.LESS_THAN_10).difficulty(Difficulty.EASY).serving(Serving.FOUR)
                        .build()).build();
        recipeDocumentRepository.saveAll(Arrays.asList(recipe1, recipe2));

        // when : 검색 조건의 recipeInfo 항목을 UNDEFINED로 설정한 뒤 검색한다.
        List<RecipeDocument> found = recipeDocumentQueryService
                .findByRecipeInfo("", CookingTime.UNDEFINED, Difficulty.UNDEFINED, Serving.UNDEFINED);
        List<Long> foundIds = found.stream().map(RecipeDocument::getId).toList();

        // then : recipe1, recipe2가 모두 검색된다.
        assertTrue(foundIds.containsAll(Arrays.asList(recipe1.getId(), recipe2.getId())));
    }
}

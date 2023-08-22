package com.recipetory.tag.application;

import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.tag.domain.TagName;
import com.recipetory.tag.domain.TagRepository;
import com.recipetory.tag.presentation.dto.TagDto;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.exception.NotOwnerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    private TagService tagService;
    private TagRepository tagRepository;
    @Mock
    private UserService userService;
    @Mock
    private RecipeService recipeService;

    User testUser;
    String testEmail = "test@test.com";

    @BeforeEach
    void setUp() {
        tagRepository = new TestTagRepository();
        tagService = new TagService(
                tagRepository, recipeService, userService);

        testUser = User.builder().id(1L)
                        .name("test").email(testEmail).role(Role.USER).build();
    }

    @Test
    @DisplayName("레시피 작성자는 태그를 따로 추가하고 삭제할 수 있다.")
    public void addTagTest() {
        when(userService.getUserByEmail(testEmail)).thenReturn(testUser);

        // given : testUser의 레시피와 tagDto를 생성한다.
        Recipe testRecipe = getSavedTestRecipe();
        when(recipeService.getRecipeById(any())).thenReturn(testRecipe);
        TagDto tagDto = TagDto.builder().tagName(TagName.BOIL).build();

        // when : 생성한 레시피에 태그를 붙이고 삭제할 수 있다.
        TagDto saved = tagService.addTag(tagDto, testRecipe.getId(), testEmail);
        assertDoesNotThrow(() -> tagService.deleteTag(saved.getTagId(),testEmail));
    }

    @Test
    @DisplayName("레시피 태그를 추가할 수 있는 사람은 레시피 작성자이다.")
    public void tagAuthorAddTest() {
        // given1 : testUser의 레시피를 생성한다.
        Recipe testRecipe = getSavedTestRecipe();
        when(recipeService.getRecipeById(any())).thenReturn(testRecipe);

        // given2 : 레시피 작성자가 아닌 anotherUser가 tagDto를 생성된 레시피에 추가하려고 한다.
        User anotherUser = User.builder().id(2L)
                .name("another").email("another@test.com").role(Role.USER).build();
        when(userService.getUserByEmail(anotherUser.getEmail())).thenReturn(anotherUser);
        TagDto tagDto = TagDto.builder().tagName(TagName.BOIL).build();

        // when, then : NotOwnerException이 발생한다.
        assertThrows(NotOwnerException.class, () ->
                tagService.addTag(tagDto, testRecipe.getId(), anotherUser.getEmail()));
    }

    @Test
    @DisplayName("레시피 태그를 삭제할 수 있는 사람은 레시피 작성자이다.")
    public void tagAuthorDeleteTest() {
        when(userService.getUserByEmail(testEmail)).thenReturn(testUser);

        // given1 : testUser의 레시피에 태그를 더한다.
        Recipe testRecipe = getSavedTestRecipe();
        when(recipeService.getRecipeById(any())).thenReturn(testRecipe);
        TagDto saved = tagService.addTag(TagDto.builder().tagName(TagName.BOIL).build(),
                testRecipe.getId(),testEmail);

        // given2 : 레시피 작성자가 아닌 anotherUser
        User anotherUser = User.builder().id(2L)
                .name("another").email("another@test.com").role(Role.USER).build();
        when(userService.getUserByEmail(anotherUser.getEmail())).thenReturn(anotherUser);

        // when, then : anotherUser가 생성된 태그를 삭제하려고 하면 NotOwnerException이 발생한다.
        assertThrows(NotOwnerException.class, () ->
                tagService.deleteTag(saved.getTagId(), anotherUser.getEmail()));
    }

    @Test
    @DisplayName("레시피에 달린 태그로 레시피 검색이 가능하다.")
    public void tagSearchTest() {
        when(userService.getUserByEmail(testEmail)).thenReturn(testUser);

        // given : recipe1, recipe2는 BREAD 태그를,
        //         recipe3는 CANDY 태그를 갖는다.
        Recipe recipe1 = getSavedTestRecipe();
        when(recipeService.getRecipeById(recipe1.getId())).thenReturn(recipe1);
        Recipe recipe2 = getSavedTestRecipe();
        when(recipeService.getRecipeById(recipe2.getId())).thenReturn(recipe2);
        Recipe recipe3 = getSavedTestRecipe();
        when(recipeService.getRecipeById(recipe3.getId())).thenReturn(recipe3);
        tagService.addTag(TagDto.builder().tagName(TagName.BREAD).build(),
                recipe1.getId(), testEmail);
        tagService.addTag(TagDto.builder().tagName(TagName.BREAD).build(),
                recipe2.getId(), testEmail);
        tagService.addTag(TagDto.builder().tagName(TagName.CANDY).build(),
                recipe3.getId(), testEmail);

        // when : BREAD 태그로 Recipe 조회 요청을 한다.
        List<Recipe> found = tagService.getRecipeByTag(TagName.BREAD);

        // then : recipe1, recipe2만이 쿼리 결과에 존재한다.
        assertAll(() -> assertTrue(found.contains(recipe1)),
                () -> assertTrue(found.contains(recipe2)),
                () -> assertFalse(found.contains(recipe3)));
    }

    @Test
    @DisplayName("같은 태그를 여러 번 추가해도 한 번만 반영된다.")
    public void idempotentTagTest() {
        when(userService.getUserByEmail(testEmail)).thenReturn(testUser);

        // given : testRecipe를 생성한다.
        Recipe testRecipe = getSavedTestRecipe();
        when(recipeService.getRecipeById(testRecipe.getId())).thenReturn(testRecipe);

        // when : 생성된 testRecipe에 동일한 태그를 5번 추가한다.
        for (int i=0; i<5; i++) {
            tagService.addTag(TagDto.builder().tagName(TagName.DRINK).build(),
                    testRecipe.getId(), testEmail);
        }

        // then : testRecipe에 추가된 태그는 1개이다.
        assertEquals(1, tagRepository.findByRecipe(testRecipe).size());
    }

    /**
     * id를 더해서 created recipe를 반환한다.
     */
    private final AtomicLong atomicLong = new AtomicLong(1L);
    private Recipe getSavedTestRecipe() {
        return Recipe.builder()
                .id(atomicLong.getAndIncrement())
                .title("test").author(testUser)
                .steps(new ArrayList<>()).tags(new ArrayList<>())
                .build();
    }
}

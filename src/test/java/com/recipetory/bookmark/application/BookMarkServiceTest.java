package com.recipetory.bookmark.application;


import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.bookmark.domain.BookMarkRepository;
import com.recipetory.bookmark.domain.exception.CannotBookMarkException;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeStatistics;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookMarkServiceTest {
    private BookMarkService bookMarkService;
    @Mock
    private UserService userService;
    @Mock
    private RecipeService recipeService;

    User user1, user2;
    Recipe recipe1, recipe2;

    @BeforeEach
    public void setUp() {
        BookMarkRepository testBookMarkRepository = new TestBookMarkRepository();
        bookMarkService = new BookMarkService(testBookMarkRepository,userService,recipeService);

        // user1이 recipe1
        // user2가 recipe2 작성
        user1 = User.builder().id(1L).email("user1@test.com")
                .name("user1").role(Role.USER).build();
        user2 = User.builder().email("user2@test.com")
                .id(2L).name("user2").role(Role.USER).build();
        recipe1 = Recipe.builder().author(user1)
                .id(1L).title("recipe1").recipeStatistics(new RecipeStatistics()).build();
        recipe2 = Recipe.builder().author(user2)
                .id(2L).title("recipe2").recipeStatistics(new RecipeStatistics()).build();
    }

    @Test
    @DisplayName("자신이 작성하지 않은 레시피의 북마크에 성공한다.")
    public void bookMarkTest() {
        when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
        when(recipeService.getRecipeById(recipe2.getId())).thenReturn(recipe2);

        assertDoesNotThrow(() -> {
            bookMarkService.addBookMark(user1.getEmail(),recipe2.getId());
        });
    }

    @Test
    @DisplayName("레시피의 주인은 북마크할 수 없다.")
    public void cannotReBookMarkTest() {
        when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
        when(recipeService.getRecipeById(recipe1.getId())).thenReturn(recipe1);

        assertThrows(CannotBookMarkException.class, () -> {
            bookMarkService.addBookMark(user1.getEmail(),recipe1.getId());
        });
    }

    @Test
    @DisplayName("같은 레시피를 여러번 북마크해도 한 번만 반영된다.(멱등하다)")
    public void idempotentBookMarkTest() {
        when(userService.getUserByEmail(user1.getEmail())).thenReturn(user1);
        when(recipeService.getRecipeById(recipe2.getId())).thenReturn(recipe2);

        BookMark saved = bookMarkService.addBookMark(user1.getEmail(),recipe2.getId());

        assertEquals(saved,
                bookMarkService.addBookMark(user1.getEmail(),recipe2.getId()));
    }
}

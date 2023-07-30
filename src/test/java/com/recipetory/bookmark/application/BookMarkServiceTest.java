package com.recipetory.bookmark.application;


import com.recipetory.TestRepositoryConfig;
import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.bookmark.domain.BookMarkRepository;
import com.recipetory.bookmark.domain.exception.CannotBookMarkException;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestRepositoryConfig.class)
public class BookMarkServiceTest {
    private BookMarkService bookMarkService;

    @Autowired
    private BookMarkRepository bookMarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    User user1, user2;
    Recipe recipe1, recipe2;
    String userEmail1 = "user1@test.com";
    String userEmail2 = "user2@test.com";

    @BeforeEach
    public void setUp() {
        bookMarkService = new BookMarkService(
                bookMarkRepository, userRepository, recipeRepository);

        user1 = userRepository.save(
                User.builder()
                        .name("user1")
                        .role(Role.USER)
                        .email(userEmail1)
                        .build());
        user2 = userRepository.save(
                User.builder()
                        .name("user2")
                        .role(Role.USER)
                        .email(userEmail2)
                        .build());

        recipe1 = recipeRepository.save(
                Recipe.builder()
                        .title("recipe1")
                        .author(user1)
                        .build());
        recipe2 = recipeRepository.save(
                Recipe.builder()
                        .title("recipe2")
                        .author(user2)
                        .build());
    }

    @Test
    @DisplayName("자신이 작성하지 않은 레시피의 북마크에 성공한다.")
    public void bookMarkTest() {
        assertDoesNotThrow(() -> {
            bookMarkService.addBookMark(userEmail1,recipe2.getId());
        });
        assertNotNull(bookMarkRepository.findByBookMarkerAndRecipe(user1,recipe2));
    }

    @Test
    @DisplayName("레시피의 주인은 북마크할 수 없다.")
    public void cannotReBookMarkTest() {
        assertThrows(CannotBookMarkException.class, () -> {
            bookMarkService.addBookMark(userEmail1,recipe1.getId());
        });
    }

    @Test
    @DisplayName("같은 레시피를 여러번 북마크해도 한 번만 반영된다.(멱등하다)")
    public void idempotentBookMarkTest() {
        BookMark saved = bookMarkService.addBookMark(userEmail1,recipe2.getId());
        assertEquals(saved,
                bookMarkService.addBookMark(userEmail1,recipe2.getId()));
    }
}

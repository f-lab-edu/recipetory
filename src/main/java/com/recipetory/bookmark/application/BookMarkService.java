package com.recipetory.bookmark.application;

import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.bookmark.domain.BookMarkRepository;
import com.recipetory.bookmark.domain.exception.CannotBookMarkException;
import com.recipetory.recipe.application.RecipeService;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.presentation.dto.RecipeDto;
import com.recipetory.recipe.presentation.dto.RecipeListDto;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookMarkService {
    private final BookMarkRepository bookMarkRepository;
    private final UserService userService;
    private final RecipeService recipeService;

    @Transactional
    public BookMark addBookMark(String logInEmail, Long recipeId) {
        User user = userService.getUserByEmail(logInEmail);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (recipe.isSameAuthor(user)) {
            throw new CannotBookMarkException(user.getId(),recipe.getId());
        }

        return bookMarkRepository.findByBookMarkerAndRecipe(user, recipe)
                .orElseGet(() -> {
                    recipe.addBookMarkCount();
                    return bookMarkRepository.save(BookMark.builder()
                        .bookMarker(user).recipe(recipe)
                        .build());
                });
    }

    @Transactional(readOnly = true)
    public RecipeListDto findBookMarkRecipeByUserId(Long userId) {
        User bookMarker = userService.getUserById(userId);
        List<BookMark> bookMarks = bookMarkRepository.findByBookMarker(bookMarker);
        List<RecipeDto> bookMarkRecipes = bookMarks.stream()
                .map(bookMark -> recipeService.getCompleteRecipe(bookMark.getRecipe().getId()))
                .toList();
        return new RecipeListDto(bookMarkRecipes);
    }

    @Transactional(readOnly = true)
    public List<BookMark> findBookMarkByRecipeId(Long recipeId) {
        Recipe found = recipeService.getRecipeById(recipeId);

        return bookMarkRepository.findByRecipe(found);
    }

    @Transactional
    public void deleteBookMark(String logInEmail,
            Long recipeId) {
        User foundUser = userService.getUserByEmail(logInEmail);
        Recipe foundRecipe = recipeService.getRecipeById(recipeId);

        bookMarkRepository.findByBookMarkerAndRecipe(foundUser, foundRecipe)
                .ifPresent(bookMark -> {
                    bookMarkRepository.deleteBookMark(bookMark);
                    foundRecipe.subtractBookMarkCount();
                });
    }
}

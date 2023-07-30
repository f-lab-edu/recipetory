package com.recipetory.bookmark.application;

import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.bookmark.domain.BookMarkRepository;
import com.recipetory.bookmark.domain.exception.CannotBookMarkException;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.recipe.domain.exception.RecipeNotFoundException;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserKeyType;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookMarkService {
    private final BookMarkRepository bookMarkRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    public BookMark addBookMark(String logInEmail, Long recipeId) {
        User user = getUserByEmail(logInEmail);
        Recipe recipe = getRecipeById(recipeId);

        if (recipe.isSameAuthor(user)) {
            throw new CannotBookMarkException(user.getId(),recipe.getId());
        }

        return bookMarkRepository.findByBookMarkerAndRecipe(user, recipe)
                .orElse(bookMarkRepository.save(BookMark.builder()
                        .bookMarker(user).recipe(recipe)
                        .build()));
    }

    @Transactional(readOnly = true)
    public List<BookMark> findBookMarkByUserId(Long userId) {
        User bookMarker = getUserById(userId);

        return bookMarkRepository.findByBookMarker(bookMarker);
    }

    @Transactional(readOnly = true)
    public List<BookMark> findBookMarkByRecipeId(Long recipeId) {
        Recipe found = getRecipeById(recipeId);

        return bookMarkRepository.findByRecipe(found);
    }

    @Transactional
    public void deleteBookMark(String logInEmail,
            Long recipeId) {
        User foundUser = getUserByEmail(logInEmail);
        Recipe foundRecipe = getRecipeById(recipeId);

        bookMarkRepository.findByBookMarkerAndRecipe(foundUser, foundRecipe)
                .ifPresent(bookMarkRepository::deleteBookMark);
    }


    @Transactional
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        UserKeyType.EMAIL, email));
    }

    @Transactional
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        UserKeyType.ID, String.valueOf(userId)));
    }

    @Transactional
    public Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(
                        String.valueOf(recipeId)));
    }
}

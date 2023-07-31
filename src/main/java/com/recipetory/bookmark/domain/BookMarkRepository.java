package com.recipetory.bookmark.domain;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository {
    BookMark save(BookMark bookMark);

    List<BookMark> findByBookMarker(User bookMarker);

    List<BookMark> findByRecipe(Recipe recipe);

    Optional<BookMark> findByBookMarkerAndRecipe(User bookMarker, Recipe recipe);

    Optional<BookMark> findById(Long id);

    void deleteBookMark(BookMark bookMark);
}

package com.recipetory.bookmark.infrastructure;

import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkJpaRepository extends JpaRepository<BookMark,Long> {
    List<BookMark> findByBookMarker(User bookMarker);

    List<BookMark> findByRecipe(Recipe recipe);

    Optional<BookMark> findByBookMarkerAndRecipe(User bookMarker, Recipe recipe);
}

package com.recipetory.reply.domain.review;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Review save(Review review);

    Optional<Review> findById(Long id);

    List<Review> findByAuthor(User author);

    List<Review> findByRecipe(Recipe recipe);

    void delete(Review review);
}

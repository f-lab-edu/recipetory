package com.recipetory.reply.infrastructure.review;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
    List<Review> findByAuthor(User author);

    List<Review> findByRecipe(Recipe recipe);
}

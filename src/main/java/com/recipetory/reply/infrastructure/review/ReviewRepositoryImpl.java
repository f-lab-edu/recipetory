package com.recipetory.reply.infrastructure.review;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.reply.domain.review.ReviewRepository;
import com.recipetory.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {
    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(review);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewJpaRepository.findById(id);
    }

    @Override
    public List<Review> findByAuthor(User author) {
        return reviewJpaRepository.findByAuthor(author);
    }

    @Override
    public List<Review> findByRecipe(Recipe recipe) {
        return reviewJpaRepository.findByRecipe(recipe);
    }

    @Override
    public void delete(Review review) {
        reviewJpaRepository.delete(review);
    }
}

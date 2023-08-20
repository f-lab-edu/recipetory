package com.recipetory.reply.application;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.reply.domain.review.ReviewRepository;
import com.recipetory.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TestReviewRepository implements ReviewRepository {
    private final List<Review> reviews = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong(1L);

    @Override
    public Review save(Review review) {
        Review saved = Review.builder()
                .id(atomicLong.getAndIncrement())
                .recipe(review.getRecipe()).author(review.getAuthor())
                .rating(review.getRating()).content(review.getContent())
                .build();
        reviews.add(saved);
        return saved;
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviews.stream()
                .filter(review -> review.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Review> findByAuthor(User author) {
        return reviews.stream()
                .filter(review -> review.getAuthor().equals(author))
                .toList();
    }

    @Override
    public List<Review> findByRecipe(Recipe recipe) {
        return reviews.stream()
                .filter(review -> review.getRecipe().equals(recipe))
                .toList();
    }

    @Override
    public void delete(Review review) {
        reviews.remove(review);
    }
}

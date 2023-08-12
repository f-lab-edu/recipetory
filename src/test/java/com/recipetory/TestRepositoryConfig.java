package com.recipetory;

import com.recipetory.bookmark.domain.BookMarkRepository;
import com.recipetory.bookmark.infrastructure.BookMarkJpaRepository;
import com.recipetory.bookmark.infrastructure.BookMarkRepositoryImpl;
import com.recipetory.ingredient.domain.IngredientRepository;
import com.recipetory.ingredient.domain.RecipeIngredientRepository;
import com.recipetory.ingredient.infrastructure.IngredientJpaRepository;
import com.recipetory.ingredient.infrastructure.IngredientRepositoryImpl;
import com.recipetory.ingredient.infrastructure.RecipeIngredientJpaRepository;
import com.recipetory.ingredient.infrastructure.RecipeIngredientRepositoryImpl;
import com.recipetory.recipe.domain.RecipeRepository;
import com.recipetory.recipe.infrastructure.RecipeJpaRepository;
import com.recipetory.recipe.infrastructure.RecipeRepositoryImpl;
import com.recipetory.reply.domain.comment.CommentRepository;
import com.recipetory.reply.domain.review.ReviewRepository;
import com.recipetory.reply.infrastructure.comment.CommentJpaRepository;
import com.recipetory.reply.infrastructure.comment.CommentRepositoryImpl;
import com.recipetory.reply.infrastructure.review.ReviewJpaRepository;
import com.recipetory.reply.infrastructure.review.ReviewRepositoryImpl;
import com.recipetory.step.domain.StepRepository;
import com.recipetory.step.infrastructure.StepJpaRepository;
import com.recipetory.step.infrastructure.StepRepositoryImpl;
import com.recipetory.tag.domain.TagRepository;
import com.recipetory.tag.infrastructure.TagJpaRepository;
import com.recipetory.tag.infrastructure.TagRepositoryImpl;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.follow.FollowRepository;
import com.recipetory.user.infrastructure.FollowJpaRepository;
import com.recipetory.user.infrastructure.FollowRepositoryImpl;
import com.recipetory.user.infrastructure.UserJpaRepository;
import com.recipetory.user.infrastructure.UserRepositoryImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestRepositoryConfig {
    @Bean
    public RecipeRepository recipeRepository(
            RecipeJpaRepository recipeJpaRepository) {
        return new RecipeRepositoryImpl(recipeJpaRepository);
    }
    @Bean
    public IngredientRepository ingredientRepository(
            IngredientJpaRepository ingredientJpaRepository) {
        return new IngredientRepositoryImpl(ingredientJpaRepository);
    }

    @Bean
    public UserRepository userRepository(
            UserJpaRepository userJpaRepository) {
        return new UserRepositoryImpl(userJpaRepository);
    }

    @Bean
    public BookMarkRepository bookMarkRepository(
            BookMarkJpaRepository bookMarkJpaRepository) {
        return new BookMarkRepositoryImpl(bookMarkJpaRepository);
    }

    @Bean
    public RecipeIngredientRepository recipeIngredientRepository(
            RecipeIngredientJpaRepository recipeIngredientJpaRepository) {
        return new RecipeIngredientRepositoryImpl(recipeIngredientJpaRepository);
    }

    @Bean
    public FollowRepository followRepository(
            FollowJpaRepository followJpaRepository) {
        return new FollowRepositoryImpl(followJpaRepository);
    }

    @Bean
    public CommentRepository commentRepository(
            CommentJpaRepository commentJpaRepository) {
        return new CommentRepositoryImpl(commentJpaRepository);
    }

    @Bean
    public ReviewRepository reviewRepository(
            ReviewJpaRepository reviewJpaRepository) {
        return new ReviewRepositoryImpl(reviewJpaRepository);
    }

    @Bean
    public TagRepository tagRepository(
            TagJpaRepository tagJpaRepository) {
        return new TagRepositoryImpl(tagJpaRepository);
    }

    @Bean
    public StepRepository stepRepository(
            StepJpaRepository stepJpaRepository) {
        return new StepRepositoryImpl(stepJpaRepository);
    }
}

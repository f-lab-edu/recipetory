package com.recipetory.reply.domain.comment;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(Long id);

    Comment save(Comment comment);

    List<Comment> findByAuthor(User author);

    List<Comment> findByRecipe(Recipe recipe);

    void delete(Comment comment);

    int countByRecipe(Recipe recipe);
}

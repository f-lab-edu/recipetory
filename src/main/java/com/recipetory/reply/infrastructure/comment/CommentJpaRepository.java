package com.recipetory.reply.infrastructure.comment;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByAuthor(User author);

    List<Comment> findByRecipe(Recipe recipe);

    int countByRecipe(Recipe recipe);
}

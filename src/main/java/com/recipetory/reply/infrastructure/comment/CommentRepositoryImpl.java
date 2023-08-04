package com.recipetory.reply.infrastructure.comment;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.reply.domain.comment.CommentRepository;
import com.recipetory.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Optional<Comment> findById(Long id) {
        return commentJpaRepository.findById(id);
    }

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public List<Comment> findByAuthor(User author) {
        return commentJpaRepository.findByAuthor(author);
    }

    @Override
    public List<Comment> findByRecipe(Recipe recipe) {
        return commentJpaRepository.findByRecipe(recipe);
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(comment);
    }

    @Override
    public int countByRecipe(Recipe recipe) {
        return commentJpaRepository.countByRecipe(recipe);
    }
}

package com.recipetory.reply.application;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.reply.domain.comment.CommentRepository;
import com.recipetory.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TestCommentRepository implements CommentRepository {
    private final List<Comment> comments = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong(1L);

    @Override
    public Optional<Comment> findById(Long id) {
        return comments.stream()
                .filter(comment -> comment.getId().equals(id)).findAny();
    }

    @Override
    public Comment save(Comment comment) {
        Comment created = Comment.builder()
                .id(atomicLong.getAndIncrement())
                .author(comment.getAuthor()).recipe(comment.getRecipe()).content(comment.getContent())
                .build();
        comments.add(created);
        return created;
    }

    @Override
    public List<Comment> findByAuthor(User author) {
        return comments.stream()
                .filter(comment -> comment.getAuthor().equals(author)).toList();
    }

    @Override
    public List<Comment> findByRecipe(Recipe recipe) {
        return comments.stream()
                .filter(comment -> comment.getRecipe().equals(recipe)).toList();
    }

    @Override
    public void delete(Comment comment) {
        comments.remove(comment);
    }

    @Override
    public int countByRecipe(Recipe recipe) {
        return (int) comments.stream()
                .filter(comment -> comment.getRecipe().equals(recipe)).count();
    }
}

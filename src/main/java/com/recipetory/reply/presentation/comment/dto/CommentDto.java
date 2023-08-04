package com.recipetory.reply.presentation.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.presentation.dto.SimpleRecipeDto;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.user.domain.User;
import com.recipetory.user.presentation.dto.SimpleUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    private Long commentId;
    private SimpleUserDto author;
    private SimpleRecipeDto recipe;
    private String content;

    public static CommentDto fromEntity(Comment comment) {
        User author = comment.getAuthor();
        Recipe recipe = comment.getRecipe();

        return CommentDto.builder()
                .commentId(comment.getId())
                .author(SimpleUserDto.fromEntity(author))
                .recipe(SimpleRecipeDto.fromEntity(recipe))
                .content(comment.getContent())
                .build();
    }
}

package com.recipetory.reply.presentation.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.recipe.presentation.dto.RecipeDto;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.user.domain.User;
import com.recipetory.user.presentation.dto.UserDto;
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
    private UserDto author;
    private RecipeDto recipe;
    private String content;

    public static CommentDto fromEntity(Comment comment) {
        User author = comment.getAuthor();
        Recipe recipe = comment.getRecipe();

        return CommentDto.builder()
                .commentId(comment.getId())
                .author(UserDto.fromEntity(author))
                .recipe(RecipeDto.fromEntity(recipe))
                .content(comment.getContent())
                .build();
    }
}

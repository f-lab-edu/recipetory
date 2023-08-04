package com.recipetory.reply.presentation.comment.dto;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateCommentDto {
    @NotNull(message = "댓글을 추가할 레시피 아이디가 필요합니다.")
    private Long recipeId;

    @NotNull(message = "생성할 댓글 내용이 필요합니다.")
    @NotBlank(message = "생성할 댓글 내용은 공란이어선 안됩니다.")
    @Length(max = Comment.MAX_CONTENT_LENGTH,
            message = "댓글은 최대 " + Comment.MAX_CONTENT_LENGTH + "자까지 가능합니다.")
    private String content;

    public Comment toEntity(User author, Recipe recipe) {
        return Comment.builder()
                .author(author)
                .recipe(recipe)
                .content(content)
                .build();
    }
}

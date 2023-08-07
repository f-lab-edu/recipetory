package com.recipetory.reply.presentation.comment.dto;

import com.recipetory.reply.domain.comment.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateCommentDto {
    @NotNull(message = "수정할 댓글 내용이 필요합니다.")
    @NotBlank(message = "수정할 댓글 내용은 공란이어선 안됩니다.")
    @Length(max = Comment.MAX_CONTENT_LENGTH,
            message = "댓글은 최대 " + Comment.MAX_CONTENT_LENGTH + "자까지 가능합니다.")
    private String content;
}

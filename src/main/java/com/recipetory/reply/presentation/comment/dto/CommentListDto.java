package com.recipetory.reply.presentation.comment.dto;

import com.recipetory.reply.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentListDto {
    private List<CommentDto> comments;

    public static CommentListDto fromEntityList(List<Comment> comments) {
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentDto::fromEntity).toList();
        return new CommentListDto(commentDtos);
    }
}

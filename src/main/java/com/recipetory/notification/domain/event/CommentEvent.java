package com.recipetory.notification.domain.event;

import com.recipetory.reply.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentEvent {
    private final Comment comment;
}

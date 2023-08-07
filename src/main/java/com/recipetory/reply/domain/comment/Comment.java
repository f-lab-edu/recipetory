package com.recipetory.reply.domain.comment;

import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.presentation.comment.dto.UpdateCommentDto;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.exception.NotOwnerException;
import com.recipetory.utils.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글
 * */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Recipe.class,
            fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,
            name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(targetEntity = User.class,
            fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,
            name = "author_id")
    private User author;

    @Column(length = MAX_CONTENT_LENGTH)
    private String content;

    public void setAuthor(User author) {
        this.author = author;
    }


    public static final int MAX_CONTENT_LENGTH = 1000;

    public void verifyAuthor(User user) {
        if (this.author != user) {
            throw new NotOwnerException(
                    user.getId(),
                    author.getId(),
                    this.getClass().getName(),
                    String.valueOf(this.id));
        }
    }

    public void updateContent(UpdateCommentDto dto) {
        this.content = dto.getContent();
    }
}

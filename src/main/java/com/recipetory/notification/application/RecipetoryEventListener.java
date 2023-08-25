package com.recipetory.notification.application;

import com.recipetory.notification.domain.NotificationMessageSender;
import com.recipetory.notification.domain.NotificationType;
import com.recipetory.notification.domain.event.CreateCommentEvent;
import com.recipetory.notification.domain.event.CreateRecipeEvent;
import com.recipetory.notification.domain.event.CreateReviewEvent;
import com.recipetory.notification.domain.event.FollowEvent;
import com.recipetory.notification.presentation.dto.NotificationMessage;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.follow.Follow;
import com.recipetory.user.domain.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipetoryEventListener {
    private final NotificationMessageSender notificationMessageSender;

    /**
     * 팔로워들에게 레시피 작성을 알린다.
     * @param createRecipeEvent
     */
    @TransactionalEventListener
    @Transactional(readOnly = true)
    @Async
    public void handleCreateRecipeEvent(CreateRecipeEvent createRecipeEvent) {
        // 생성된 레시피, 알림 종류 정보 구성
        Recipe createdRecipe = createRecipeEvent.getRecipe();
        User author = createdRecipe.getAuthor();
        NotificationType type = NotificationType.NEW_RECIPE;

        // logging
        log.info("{} created recipe {}", author.getId(), createdRecipe.getId());

        NotificationMessage message = NotificationMessage.builder()
                .notificationType(NotificationType.NEW_RECIPE)
                .path(NotificationType.NEW_RECIPE.getDefaultPath(author.getId()))
                .senderId(author.getId()).build();

        notificationMessageSender.sendNotificationMessage(message);

    }

    /**
     * 팔로우 당한 사람에게 팔로우 알림
     * @param followEvent
     */
    @TransactionalEventListener
    @Transactional(readOnly = true)
    @Async
    public void handleFollowEvent(FollowEvent followEvent) {
        Follow follow = followEvent.getFollow();
        User sender = follow.getFollowing();
        User receiver = follow.getFollowed();

        log.info("{} follows {}", sender.getId(), receiver.getId());

        NotificationMessage message = NotificationMessage.builder()
                .notificationType(NotificationType.FOLLOW)
                .path(NotificationType.FOLLOW.getDefaultPath(sender.getId()))
                .senderId(sender.getId()).receiverId(receiver.getId())
                .build();

        notificationMessageSender.sendNotificationMessage(message);
    }

    /**
     * 레시피를 작성한 사람에게 댓글 알림
     * @param createCommentEvent
     */
    @TransactionalEventListener
    @Transactional(readOnly = true)
    @Async
    public void handleCreateCommentEvent(CreateCommentEvent createCommentEvent) {
        Comment comment = createCommentEvent.getComment();
        User sender = comment.getAuthor();
        User receiver = comment.getRecipe().getAuthor();
        NotificationType type = NotificationType.COMMENT;

        log.info("{} comments {}", sender.getId(), comment.getId());

        NotificationMessage message = NotificationMessage.builder()
                .notificationType(type).path(type.getDefaultPath(comment.getId()))
                .senderId(sender.getId()).receiverId(receiver.getId())
                .build();

        notificationMessageSender.sendNotificationMessage(message);
    }

    /**
     * 레시피를 작성한 사람에게 리뷰 알림
     * @param createReviewEvent
     */
    @TransactionalEventListener
    @Transactional(readOnly = true)
    @Async
    public void handleCreateReviewEvent(CreateReviewEvent createReviewEvent) {
        Review review = createReviewEvent.getReview();
        User sender = review.getAuthor();
        User receiver = review.getRecipe().getAuthor();
        NotificationType type = NotificationType.REVIEW;

        log.info("{} reviewed {}", sender.getId(), review.getId());

        NotificationMessage message = NotificationMessage.builder()
                .notificationType(type).path(type.getDefaultPath(review.getId()))
                .senderId(sender.getId()).receiverId(receiver.getId())
                .build();

        notificationMessageSender.sendNotificationMessage(message);
    }
}

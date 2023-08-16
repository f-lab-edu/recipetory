package com.recipetory.notification.application;

import com.recipetory.notification.domain.Notification;
import com.recipetory.notification.domain.NotificationRepository;
import com.recipetory.notification.domain.NotificationType;
import com.recipetory.notification.domain.event.CreateCommentEvent;
import com.recipetory.notification.domain.event.CreateRecipeEvent;
import com.recipetory.notification.domain.event.FollowEvent;
import com.recipetory.notification.domain.event.CreateReviewEvent;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.reply.domain.comment.Comment;
import com.recipetory.reply.domain.review.Review;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.follow.Follow;
import com.recipetory.user.domain.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipetoryEventListener {
    private final NotificationRepository notificationRepository;
    private final FollowRepository followRepository;

    /**
     * 팔로워들에게 레시피 작성을 알린다.
     * @param createRecipeEvent
     */
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCreateRecipeEvent(CreateRecipeEvent createRecipeEvent) {
        Recipe createdRecipe = createRecipeEvent.getRecipe();
        User author = createdRecipe.getAuthor();

        log.info("{} creates recipe {}", author.getId(), createdRecipe.getId());

        followRepository.findByFollowed(author)
                .forEach(follow -> {
                    User follower = follow.getFollowing();

                    notificationRepository.save(Notification.builder()
                            .sender(author).receiver(follower)
                            .notificationType(NotificationType.NEW_RECIPE)
                            .message(NotificationType.NEW_RECIPE.getDefaultMessage(author))
                            .path("/recipes/" + createdRecipe.getId())
                            .build());
                });
    }

    /**
     * 팔로우 알림
     * @param followEvent
     */
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleFollowEvent(FollowEvent followEvent) {
        Follow follow = followEvent.getFollow();
        User sender = follow.getFollowing();
        User receiver = follow.getFollowed();

        log.info("{} follows {}", sender.getId(), receiver.getId());

        notificationRepository.save(Notification.builder()
                .notificationType(NotificationType.FOLLOW)
                .sender(sender).receiver(receiver)
                .message(NotificationType.FOLLOW.getDefaultMessage(sender))
                .path("/"+sender.getId())
                .build());
    }

    /**
     * 댓글 알림
     * @param createCommentEvent
     */
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCommentEvent(CreateCommentEvent createCommentEvent) {
        Comment comment = createCommentEvent.getComment();
        User sender = comment.getAuthor();
        User receiver = comment.getRecipe().getAuthor();

        log.info("{} comments {}", sender.getId(), comment.getId());

        notificationRepository.save(Notification.builder()
                .sender(sender).receiver(receiver)
                .notificationType(NotificationType.COMMENT)
                .message(NotificationType.COMMENT.getDefaultMessage(sender))
                .path("/comment/" + comment.getId())
                .build());
    }

    /**
     * 리뷰 알림
     * @param createReviewEvent
     */
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCommentEvent(CreateReviewEvent createReviewEvent) {
        Review review = createReviewEvent.getReview();
        User sender = review.getAuthor();
        User receiver = review.getRecipe().getAuthor();

        log.info("{} reviewed {}", sender.getId(), review.getId());

        notificationRepository.save(Notification.builder()
                .sender(sender).receiver(receiver)
                .notificationType(NotificationType.REVIEW)
                .message(NotificationType.REVIEW.getDefaultMessage(sender))
                .path("/review/" + review.getId())
                .build());
    }
}

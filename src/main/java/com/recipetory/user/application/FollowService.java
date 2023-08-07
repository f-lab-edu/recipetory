package com.recipetory.user.application;

import com.recipetory.user.domain.User;
import com.recipetory.user.domain.exception.CannotFollowException;
import com.recipetory.user.domain.follow.Follow;
import com.recipetory.user.domain.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<User> getFollowers(Long userId) {
        User found = userService.getUserById(userId);

        // found 유저를 팔로우 하고 있는 사람들
        return followRepository.findByFollowed(found).stream()
                .map(Follow::getFollowing).toList();
    }

    @Transactional(readOnly = true)
    public List<User> getFollowings(Long userId) {
        User found = userService.getUserById(userId);

        // found 유저가 팔로우하는 사람들
        return followRepository.findByFollowing(found).stream()
                .map(Follow::getFollowed).toList();
    }

    @Transactional
    public Follow follow(String followingEmail, Long followedId) {
        User following = userService.getUserByEmail(followingEmail);
        User followed = userService.getUserById(followedId);

        if (followed == following) {
            throw new CannotFollowException(following.getId(), followed.getId());
        }

        return followRepository.findByFollowingAndFollowed(following,followed)
                .orElse(followRepository.save(
                        Follow.builder()
                                .following(following)
                                .followed(followed)
                                .build()));
    }

    @Transactional
    public void unFollow(String followingEmail, Long followedId) {
        User following = userService.getUserByEmail(followingEmail);
        User followed = userService.getUserById(followedId);

        followRepository.findByFollowingAndFollowed(following,followed)
                .ifPresent(followRepository::delete);
    }
}

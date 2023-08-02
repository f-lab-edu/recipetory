package com.recipetory.user.domain.follow;

import com.recipetory.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface FollowRepository {
    List<Follow> findByFollowing(User follower);

    List<Follow> findByFollowed(User followed);

    Optional<Follow> findByFollowingAndFollowed(User following, User followed);

    Follow save(Follow follow);

    void delete(Follow follow);
}

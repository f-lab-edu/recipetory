package com.recipetory.user.service;

import com.recipetory.user.domain.User;
import com.recipetory.user.domain.follow.Follow;
import com.recipetory.user.domain.follow.FollowRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestFollowRepository implements FollowRepository {
    private final List<Follow> follows = new ArrayList<>();

    @Override
    public List<Follow> findByFollowing(User follower) {
        return follows.stream()
                .filter(follow -> follow.getFollowing().equals(follower))
                .toList();
    }

    @Override
    public List<Follow> findByFollowed(User followed) {
        return follows.stream()
                .filter(follow -> follow.getFollowed().equals(followed))
                .toList();
    }

    @Override
    public Optional<Follow> findByFollowingAndFollowed(User following, User followed) {
        return follows.stream()
                .filter(follow -> follow.getFollowed().equals(followed)
                        && follow.getFollowing().equals(following))
                .findAny();
    }

    @Override
    public Follow save(Follow follow) {
        follows.add(follow);
        return follow;
    }

    @Override
    public void delete(Follow follow) {
        follows.remove(follow);
    }
}

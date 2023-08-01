package com.recipetory.user.infrastructure;

import com.recipetory.user.domain.User;
import com.recipetory.user.domain.follow.Follow;
import com.recipetory.user.domain.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class FollowRepositoryImpl implements FollowRepository {
    private final FollowJpaRepository followJpaRepository;

    @Override
    public List<Follow> findByFollowing(User following) {
        return followJpaRepository.findByFollowing(following);
    }

    @Override
    public List<Follow> findByFollowed(User followed) {
        return followJpaRepository.findByFollowed(followed);
    }

    @Override
    public Optional<Follow> findByFollowingAndFollowed(User following, User followed) {
        return followJpaRepository.findByFollowingAndFollowed(following,followed);
    }

    @Override
    public Follow save(Follow follow) {
        return followJpaRepository.save(follow);
    }

    @Override
    public void delete(Follow follow) {
        followJpaRepository.delete(follow);
    }
}

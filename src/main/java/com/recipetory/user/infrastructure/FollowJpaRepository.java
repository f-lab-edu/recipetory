package com.recipetory.user.infrastructure;

import com.recipetory.user.domain.User;
import com.recipetory.user.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowJpaRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollowing(User following);

    List<Follow> findByFollowed(User followed);

    Optional<Follow> findByFollowingAndFollowed(User following, User followed);
}

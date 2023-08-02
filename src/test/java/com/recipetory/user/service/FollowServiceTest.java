package com.recipetory.user.service;

import com.recipetory.TestRepositoryConfig;
import com.recipetory.user.application.FollowService;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.exception.CannotFollowException;
import com.recipetory.user.domain.follow.Follow;
import com.recipetory.user.domain.follow.FollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestRepositoryConfig.class)
public class FollowServiceTest {
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    private FollowService followService;

    User user1, user2;


    @BeforeEach
    public void setUp() {
        followService = new FollowService(followRepository,userRepository);

        user1 = userRepository.save(
                User.builder().name("user1").role(Role.USER).email("user1@test.com").build());
        user2 = userRepository.save(
                User.builder().name("user2").role(Role.USER).email("user2@test.com").build());
    }

    @Test
    @DisplayName("팔로우는 성공한다.")
    public void followTest() {
        // given : user1이 user2를 팔로우한다.
        followService.follow(user1.getEmail(),user2.getId());

        // when : 팔로우 기록을 찾는다.
        Follow foundFollow = followRepository.findByFollowingAndFollowed(
                user1, user2).get();

        // then : 모든 필드가 잘 설정되었다.
        assertNotNull(foundFollow);
        assertEquals(foundFollow.getFollowing(), user1);
        assertEquals(foundFollow.getFollowed(), user2);
    }

    @Test
    @DisplayName("자기자신을 팔로우하면 CannotFollowException 발생한다.")
    public void cannotFollowMe() {
        assertThrows(CannotFollowException.class, () ->
                followService.follow(user1.getEmail(),user1.getId()));
    }

    @Test
    @DisplayName("같은 유저를 여러번 팔로우해도 결과는 한 번만 반영된다.")
    public void idempotentFollowTest() {
        Follow saved1 = followService.follow(user1.getEmail(), user2.getId());
        Follow saved2 = followService.follow(user1.getEmail(), user2.getId());

        assertEquals(saved1,saved2);
    }
}

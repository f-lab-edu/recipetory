package com.recipetory.user.service;

import com.recipetory.user.application.FollowService;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.exception.CannotFollowException;
import com.recipetory.user.domain.follow.Follow;
import com.recipetory.user.domain.follow.FollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
    private FollowService followService;
    private FollowRepository followRepository;
    @Mock
    private UserService userService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    User user1, user2;
    String userEmail1 = "user1@test.com";
    String userEmail2 = "user2@test.com";

    @BeforeEach
    public void setUp() {
        followRepository = new TestFollowRepository();
        followService = new FollowService(followRepository,userService,eventPublisher);


        user1 = User.builder().id(1L).email(userEmail1)
                .name("user1").role(Role.USER).build();
        user2 = User.builder().id(2L).email(userEmail2)
                .name("user2").role(Role.USER).build();
    }

    @Test
    @DisplayName("팔로우는 성공한다.")
    public void followTest() {
        when(userService.getUserByEmail(userEmail1)).thenReturn(user1);
        when(userService.getUserById(user2.getId())).thenReturn(user2);

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
        when(userService.getUserByEmail(userEmail1)).thenReturn(user1);
        when(userService.getUserById(user1.getId())).thenReturn(user1);

        assertThrows(CannotFollowException.class, () ->
                followService.follow(user1.getEmail(),user1.getId()));
    }

    @Test
    @DisplayName("같은 유저를 여러번 팔로우해도 결과는 한 번만 반영된다.")
    public void idempotentFollowTest() {
        when(userService.getUserByEmail(userEmail1)).thenReturn(user1);
        when(userService.getUserById(user2.getId())).thenReturn(user2);

        Follow saved1 = followService.follow(user1.getEmail(), user2.getId());
        Follow saved2 = followService.follow(user1.getEmail(), user2.getId());

        assertEquals(saved1,saved2);
    }
}

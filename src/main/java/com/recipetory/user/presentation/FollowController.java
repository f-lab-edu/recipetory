package com.recipetory.user.presentation;

import com.recipetory.config.auth.argumentresolver.LogInUser;
import com.recipetory.config.auth.dto.SessionUser;
import com.recipetory.user.application.FollowService;
import com.recipetory.user.domain.User;
import com.recipetory.user.presentation.dto.UserListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @GetMapping("/{userId}/followers")
    public ResponseEntity<UserListDto> showFollowers(
            @PathVariable("userId") Long userId) {
        List<User> followers = followService.getFollowers(userId);

        return ResponseEntity.ok(UserListDto.fromEntityList(followers));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<UserListDto> showFollowing(
            @PathVariable("userId") Long userId) {
        List<User> followings = followService.getFollowings(userId);

        return ResponseEntity.ok(UserListDto.fromEntityList(followings));
    }

    @PostMapping("/follow/{userId}")
    public ResponseEntity<Void> follow(
            @LogInUser SessionUser logInUser,
            @PathVariable("userId") Long userId) {
        followService.follow(logInUser.getEmail(), userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/follow/{userId}")
    public ResponseEntity<Void> unfollow(
            @LogInUser SessionUser logInUser,
            @PathVariable("userId") Long userId) {
        followService.unFollow(logInUser.getEmail(), userId);
        return ResponseEntity.ok().build();
    }
}

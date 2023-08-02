package com.recipetory.user.presentation;

import com.recipetory.bookmark.application.BookMarkService;
import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.bookmark.presentation.dto.BookMarkListDto;
import com.recipetory.config.auth.CustomOAuth2UserService;
import com.recipetory.config.auth.argumentresolver.LogInUser;
import com.recipetory.config.auth.dto.SessionUser;
import com.recipetory.user.application.FollowService;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.User;
import com.recipetory.user.presentation.dto.EditUserDto;
import com.recipetory.user.presentation.dto.FollowDto;
import com.recipetory.user.presentation.dto.ProfileDto;
import com.recipetory.user.presentation.dto.UserHomeDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final BookMarkService bookMarkService;
    private final UserService userService;
    private final FollowService followService;
    private final CustomOAuth2UserService oAuth2UserService;

    // path variable의 user information
    @GetMapping("/{userId}")
    public ResponseEntity<UserHomeDto> showUser(
            @PathVariable("userId") Long userId) {

        return ResponseEntity.ok(
                UserHomeDto.fromEntity(
                        userService.getUserById(userId)));
    }

    @GetMapping("/{userId}/bookmark")
    public ResponseEntity<BookMarkListDto> findBookMarksOfUser(
            @PathVariable("userId") Long userId) {
        List<BookMark> foundBookMarks = bookMarkService
                .findBookMarkByUserId(userId);

        return ResponseEntity.ok(
                BookMarkListDto.fromEntityList(foundBookMarks));
    }

    // 로그인한 유저 자신의 profile
    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> showProfile(
            @LogInUser SessionUser logInUser) {

        return ResponseEntity.ok(ProfileDto.fromEntity(
                userService.getUserByEmail(logInUser.getEmail())));
    }

    @PutMapping("/profile")
    public ResponseEntity<EditUserDto.Response> editCurrentUser(
            @LogInUser SessionUser logInUser,
            @Valid @RequestBody EditUserDto editUserDto
            ) {
        String oldName = logInUser.getName();
        User edited = userService.editUser(logInUser.getEmail(), editUserDto);
        String newName = edited.getName();

        oAuth2UserService.updateContextUser(edited);

        return ResponseEntity.ok(
                EditUserDto.Response.builder()
                        .id(edited.getId())
                        .oldName(oldName).newName(newName).
                        build());
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<FollowDto> showFollowers(
            @PathVariable("userId") Long userId) {
        List<User> followers = followService.getFollowers(userId);

        return ResponseEntity.ok(FollowDto.fromEntityList(followers));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<FollowDto> showFollowing(
            @PathVariable("userId") Long userId) {
        List<User> followings = followService.getFollowings(userId);

        return ResponseEntity.ok(FollowDto.fromEntityList(followings));
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

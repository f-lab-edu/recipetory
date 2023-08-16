package com.recipetory.user.presentation;

import com.recipetory.config.auth.argumentresolver.LogInUser;
import com.recipetory.config.auth.dto.SessionUser;
import com.recipetory.user.application.UserService;
import com.recipetory.user.presentation.dto.EditUserDto;
import com.recipetory.user.presentation.dto.ProfileDto;
import com.recipetory.user.presentation.dto.UserDto;
import com.recipetory.user.presentation.dto.UserListDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * path variable의 id에 해당하는 유저의 정보를 조회한다.
     * @param userId path variable
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> showUser(
            @PathVariable("userId") Long userId) {
        UserDto found = UserDto.fromEntity(
                userService.getUserById(userId));

        return ResponseEntity.ok(found);
    }

    /**
     * 현제 세션에 로그인된 유저의 프로필 정보를 조회한다.
     * @param logInUser
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> showProfile(
            @LogInUser SessionUser logInUser) {
        ProfileDto profile = ProfileDto.fromEntity(
                userService.getUserByEmail(logInUser.getEmail()));

        return ResponseEntity.ok(profile);
    }

    /**
     * 현재 로그인한 유저의 name, bio를 수정한다.
     * @param logInUser 세션유저
     * @param editUserDto
     * @return
     */
    @PutMapping("/profile")
    public ResponseEntity<UserDto> editCurrentUser(
            @LogInUser SessionUser logInUser,
            @Valid @RequestBody EditUserDto editUserDto
            ) {
        UserDto edited = userService.editUser(
                logInUser.getEmail(), editUserDto);

        return ResponseEntity.ok(edited);
    }

    /**
     * query parameter로 들어온 문자열을 포함한 유저를 검색한다.
     * @param userName
     * @return user list
     */
    @GetMapping("/user/search")
    public ResponseEntity<UserListDto> searchUser(
            @RequestParam(value = "q", defaultValue = "") String userName
    ) {
        UserListDto found = userService.findUserByNameContains(userName);
        return ResponseEntity.ok(found);
    }
}

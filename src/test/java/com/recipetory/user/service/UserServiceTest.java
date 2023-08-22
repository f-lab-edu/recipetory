package com.recipetory.user.service;

import com.recipetory.config.auth.CustomOAuth2UserService;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.presentation.dto.EditUserDto;
import com.recipetory.utils.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserRepository userRepository;
    @Mock
    private CustomOAuth2UserService oAuth2UserService;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = new TestUserRepository();
        userService = new UserService(userRepository,oAuth2UserService);
    }

    @Test
    @DisplayName("수정한 유저의 이름과 Role은 잘 반영된다.")
    public void testEditUser() {
        // given : testEmail, "test" name 을 가진 GUEST 유저
        User user = userRepository.save(User.builder()
                .name("test").email("test@test.com").role(Role.GUEST).build());

        // when : "new test"로 edit
        String newName = "new test";
        String newBio = "new bio";
        EditUserDto editUserDto = new EditUserDto(newName,newBio);
        userService.editUser(user.getEmail(),editUserDto);

        // then : 이름, bio, 권한이 잘 수정되었다.
        User foundUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow();
        assertEquals(newName, foundUser.getName());
        assertEquals(newBio, foundUser.getBio());
        assertEquals(foundUser.getRole(),Role.USER);
    }

    @Test
    @DisplayName("존재하지 않는 유저 key로 유저를 찾을 수 없다.")
    public void testEmailNotFound() {
        // given : 현재 DB엔 유저가 없는 상태

        // when,then : 아무 이메일과 아이디로 유저 찾았을 경우 EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserByEmail("email@test.com");
        });
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(1234L);
        });
    }
}

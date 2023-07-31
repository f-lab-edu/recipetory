package com.recipetory.user.service;

import com.recipetory.TestRepositoryConfig;
import com.recipetory.user.application.UserService;
import com.recipetory.user.domain.Role;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.exception.UserNotFoundException;
import com.recipetory.user.presentation.dto.EditUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestRepositoryConfig.class)
@DataJpaTest
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("수정한 유저의 이름과 Role은 잘 반영된다.")
    public void testEditUser() {
        // given : testEmail, "test" name 을 가진 GUEST 유저
        String testEmail = "test@test.com";
        userRepository.save(User.builder()
                .name("test")
                .email("test@test.com")
                .role(Role.GUEST).build());

        // when : "new test"로 edit
        String newName = "new test";
        EditUserDto editUserDto = new EditUserDto(newName);
        userService.editUser(testEmail,editUserDto);

        // then : 이름이 "new test", 권한이 USER 유저로 바뀌었다.
        User foundUser = userRepository.findByEmail(testEmail)
                .orElseThrow();
        assertEquals(newName, foundUser.getName());
        assertEquals(foundUser.getRole(),Role.USER);
    }

    @Test
    @DisplayName("존재하지 않는 유저 key로 유저를 찾을 수 없다.")
    public void testEmailNotFound() {
        // given : 현재 DB엔 유저가 없는 상태

        // when,then : 아무 이메일과 아이디로 유저 찾았을 경우 UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByEmail("email@test.com");
        });
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1234L);
        });
    }
}

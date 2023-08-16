package com.recipetory.user.application;

import com.recipetory.config.auth.CustomOAuth2UserService;
import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.presentation.dto.EditUserDto;
import com.recipetory.user.presentation.dto.UserDto;
import com.recipetory.user.presentation.dto.UserListDto;
import com.recipetory.utils.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final CustomOAuth2UserService oAuth2UserService;

    /**
     * logInEmail을 가진 유저의 name, bio를 수정한다.
     * @param logInEmail
     * @param editUserDto
     * @return
     */
    @Transactional
    public UserDto editUser(String logInEmail, EditUserDto editUserDto) {
        User editUser = getUserByEmail(logInEmail);
        editUser.editNameAndBio(
                editUserDto.getName(), editUser.getBio());

        // security context에 수정된 유저 정보 업데이트
        oAuth2UserService.updateContextUser(editUser);

        return UserDto.builder()
                .name(editUser.getName()).bio(editUser.getBio())
                .build();
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                        new EntityNotFoundException("User",email));
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User",String.valueOf(id)));
    }

    /**
     * name 필드에 인자로 들어온 문자열을 포함한 유저를 검색한다.
     * @param name 검색할 name 문자열
     * @return user list
     */
    @Transactional(readOnly = true)
    public UserListDto findUserByNameContains(String name) {
        List<User> found = userRepository.findByNameContains(name);
        return UserListDto.fromEntityList(found);
    }
}

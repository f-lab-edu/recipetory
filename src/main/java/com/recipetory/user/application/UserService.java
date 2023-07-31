package com.recipetory.user.application;

import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserKeyType;
import com.recipetory.user.domain.UserRepository;
import com.recipetory.user.domain.exception.UserNotFoundException;
import com.recipetory.user.presentation.dto.EditUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User editUser(String logInEmail, EditUserDto editUserDto) {
        User editUser = getUserByEmail(logInEmail);
        editUser.editName(editUserDto.getName());

        return editUser;
    }

    @Transactional
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                        new UserNotFoundException(UserKeyType.EMAIL, email));
    }

    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
            new UserNotFoundException(UserKeyType.ID, String.valueOf(id)));
    }
}

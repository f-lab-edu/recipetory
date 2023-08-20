package com.recipetory.user.service;

import com.recipetory.user.domain.User;
import com.recipetory.user.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TestUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong(1L);

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email)).findAny();
    }

    @Override
    public User save(User user) {
        User saved = User.builder().id(atomicLong.getAndIncrement()).email(user.getEmail())
                .name(user.getName()).bio(user.getBio()).role(user.getRole()).build();
        users.add(saved);
        return saved;
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id)).findAny();
    }

    @Override
    public List<User> findByNameContains(String name) {
        return users.stream()
                .filter(user -> user.getName().contains(name)).toList();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(users::remove);
    }
}

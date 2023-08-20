package com.recipetory.user.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findByNameContains(String name);

    void deleteById(Long id);
}

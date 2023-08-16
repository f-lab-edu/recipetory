package com.recipetory.user.infrastructure;

import com.recipetory.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    List<User> findByNameContains(String name);
}

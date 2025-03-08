package io.github.px1d.repository;

import io.github.px1d.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User create(User user);
}

package com.it211_prj.repository;

import com.it211_prj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository thao tac CRUD va truy van dang nhap theo email.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

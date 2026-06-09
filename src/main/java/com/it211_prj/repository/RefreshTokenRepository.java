package com.it211_prj.repository;

import com.it211_prj.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository luu refresh token de xoay vong token an toan.
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(Long userId);
}

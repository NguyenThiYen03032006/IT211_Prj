package com.it211_prj.repository;

import com.it211_prj.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository kiem tra access token co bi logout/blacklist chua.
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByToken(String token);
}

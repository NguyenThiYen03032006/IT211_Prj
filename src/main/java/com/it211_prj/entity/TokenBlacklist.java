package com.it211_prj.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "token_blacklist", indexes = @Index(name = "idx_token_blacklist_token", columnList = "token"))
public class TokenBlacklist {
    // Access token da logout/bi vo hieu hoa se duoc dua vao blacklist.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 1000)
    private String token;

    // Luu thoi diem het han de co the don rac blacklist sau nay.
    @Column(nullable = false)
    private Instant expiresAt;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private Instant blacklistedAt = Instant.now();
}

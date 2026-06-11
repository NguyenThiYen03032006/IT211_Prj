package com.it211_prj.service;

import com.it211_prj.dto.auth.RefreshTokenRequest;
import com.it211_prj.dto.auth.RefreshTokenResponse;
import com.it211_prj.entity.RefreshToken;
import com.it211_prj.entity.User;
import com.it211_prj.repository.RefreshTokenRepository;
import com.it211_prj.repository.RoleRepository;
import com.it211_prj.repository.UserRepository;
import com.it211_prj.security.CustomUserDetailsService;
import com.it211_prj.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock AuthenticationManager authenticationManager;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;
    @Mock CustomUserDetailsService userDetailsService;
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock RefreshTokenRepository refreshTokenRepository;
    @Mock RedisTokenBlacklistService tokenBlacklistService;
    @Mock UserDetails userDetails;

    @InjectMocks AuthService authService;

    @Test
    void refreshRevokesOldTokenAndReturnsNewRefreshToken() {
        User user = User.builder().id(1L).email("student@it211.local").password("encoded").fullName("Student").build();
        RefreshToken oldToken = RefreshToken.builder()
                .token("old-token")
                .user(user)
                .expiresAt(Instant.now().plusSeconds(300))
                .revoked(false)
                .build();
        ReflectionTestUtils.setField(authService, "refreshTokenExpirationMs", 900000L);
        when(refreshTokenRepository.findByToken("old-token")).thenReturn(Optional.of(oldToken));
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(userDetails);
        when(jwtService.generateAccessToken(userDetails)).thenReturn("new-access-token");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshTokenResponse response = authService.refresh(new RefreshTokenRequest("old-token"));

        assertThat(oldToken.isRevoked()).isTrue();
        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.refreshToken()).isNotEqualTo("old-token");
        verify(refreshTokenRepository, times(2)).save(any(RefreshToken.class));
    }
}

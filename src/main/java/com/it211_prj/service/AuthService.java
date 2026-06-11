package com.it211_prj.service;

import com.it211_prj.dto.auth.*;
import com.it211_prj.dto.user.UserResponse;
import com.it211_prj.entity.*;
import com.it211_prj.exception.BadRequestException;
import com.it211_prj.exception.ResourceNotFoundException;
import com.it211_prj.repository.*;
import com.it211_prj.security.CustomUserDetailsService;
import com.it211_prj.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTokenBlacklistService tokenBlacklistService;

    @Value("${app.jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }
        Role role = roleRepository.findByName(RoleName.STUDENT)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.STUDENT).build()));
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .studentCode(request.studentCode())
                .enabled(true)
                .roles(Set.of(role))
                .build();
        return toUserResponse(userRepository.save(user));
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = createRefreshToken(user).getToken();
        return new LoginResponse(accessToken, refreshToken, "Bearer", toUserResponse(user));
    }

    @Transactional
    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BadRequestException("Refresh token khong hop le"));
        if (refreshToken.isRevoked() || refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException("Refresh token da het han hoac da bi thu hoi");
        }
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getUser().getEmail());
        RefreshToken newRefreshToken = createRefreshToken(refreshToken.getUser());
        return new RefreshTokenResponse(jwtService.generateAccessToken(userDetails), newRefreshToken.getToken(), "Bearer");
    }

    @Transactional
    public void logout(String accessToken, RefreshTokenRequest request) {
        if (accessToken != null && !tokenBlacklistService.isBlacklisted(accessToken)) {
            tokenBlacklistService.blacklist(accessToken, jwtService.extractExpiration(accessToken).toInstant());
        }
        refreshTokenRepository.findByToken(request.refreshToken()).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getStudentCode(), user.isEnabled(),
                user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
    }
}

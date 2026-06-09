package com.it211_prj.dto.auth;

import jakarta.validation.constraints.NotBlank;

// DTO nhan refresh token de cap access token moi.
public record RefreshTokenRequest(@NotBlank String refreshToken) {
}

package com.it211_prj.dto.auth;

// DTO tra ve access token moi sau khi refresh hop le.
public record RefreshTokenResponse(String accessToken, String refreshToken, String tokenType) {
}

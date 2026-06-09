package com.it211_prj.dto.auth;

import com.it211_prj.dto.user.UserResponse;

// DTO tra token va thong tin user sau khi dang nhap thanh cong.
public record LoginResponse(String accessToken, String refreshToken, String tokenType, UserResponse user) {
}

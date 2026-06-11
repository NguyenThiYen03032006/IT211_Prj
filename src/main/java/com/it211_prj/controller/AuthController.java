package com.it211_prj.controller;

import com.it211_prj.dto.auth.*;
import com.it211_prj.dto.user.UserResponse;
import com.it211_prj.service.AuthService;
import com.it211_prj.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    // Public API dang ky tai khoan sinh vien.
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // Public API dang nhap, tra access token va refresh token.
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // Public API refresh access token bang refresh token con hieu luc.
    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    // Logout dua access token vao blacklist va revoke refresh token.
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader(value = "Authorization", required = false) String authorization,
                       @Valid @RequestBody RefreshTokenRequest request) {
        String token = authorization != null && authorization.startsWith("Bearer ") ? authorization.substring(7) : null;
        authService.logout(token, request);
    }

    // User da dang nhap doi mat khau bang mat khau hien tai.
    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        passwordResetService.changePassword(request);
    }

    // Public API tao token quen mat khau va gui email/log token cho dev.
    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.forgotPassword(request);
    }

    // Public API dat lai mat khau bang token con hieu luc.
    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request);
    }
}

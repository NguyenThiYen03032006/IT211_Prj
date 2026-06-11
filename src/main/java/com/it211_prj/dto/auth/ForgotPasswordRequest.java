package com.it211_prj.dto.auth;

import jakarta.validation.constraints.Email;

// DTO public de yeu cau tao reset password token.
public record ForgotPasswordRequest(@Email String email) {
}

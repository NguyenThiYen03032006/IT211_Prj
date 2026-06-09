package com.it211_prj.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// DTO nhan thong tin dang nhap tu client.
public record LoginRequest(@Email String email, @NotBlank String password) {
}

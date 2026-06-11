package com.it211_prj.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO dat lai mat khau bang token da gui qua email/log.
public record ResetPasswordRequest(@NotBlank String token, @NotBlank @Size(min = 6) String newPassword) {
}

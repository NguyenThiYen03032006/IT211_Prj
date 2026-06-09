package com.it211_prj.dto.auth;

import com.it211_prj.entity.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO dang ky tai khoan moi, role mac dinh la STUDENT neu client khong gui.
public record RegisterRequest(
        @Email String email,
        @NotBlank @Size(min = 6) String password,
        @NotBlank String fullName,
        String studentCode,
        RoleName role
) {
}

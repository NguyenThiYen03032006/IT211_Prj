package com.it211_prj.dto.user;

import com.it211_prj.entity.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// DTO admin dung de tao/cap nhat user.
public record UserRequest(@Email String email, String password, @NotBlank String fullName, String studentCode, RoleName role, Boolean enabled) {
}

package com.it211_prj.dto.user;

import java.util.Set;

// DTO tra user ra API, khong bao gio tra password.
public record UserResponse(Long id, String email, String fullName, String studentCode, boolean enabled, Set<String> roles) {
}

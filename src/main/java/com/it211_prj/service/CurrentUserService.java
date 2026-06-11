package com.it211_prj.service;

import com.it211_prj.entity.User;
import com.it211_prj.exception.ResourceNotFoundException;
import com.it211_prj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    // Lay user dang dang nhap tu SecurityContext de gan vao submission, grade, material.
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay Current user"));
    }
}

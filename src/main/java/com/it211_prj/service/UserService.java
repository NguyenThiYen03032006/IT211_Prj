package com.it211_prj.service;

import com.it211_prj.dto.user.UserRequest;
import com.it211_prj.dto.user.UserResponse;
import com.it211_prj.entity.Role;
import com.it211_prj.entity.RoleName;
import com.it211_prj.entity.User;
import com.it211_prj.exception.BadRequestException;
import com.it211_prj.exception.ResourceNotFoundException;
import com.it211_prj.repository.RoleRepository;
import com.it211_prj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse findById(Long id) {
        return toResponse(findUser(id));
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }
        Role role = getRole(request.role() == null ? RoleName.STUDENT : request.role());
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .studentCode(request.studentCode())
                .enabled(request.enabled() == null || request.enabled())
                .roles(Set.of(role))
                .build();
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = findUser(id);
        user.setFullName(request.fullName());
        user.setStudentCode(request.studentCode());
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.enabled() != null) {
            user.setEnabled(request.enabled());
        }
        if (request.role() != null) {
            user.setRoles(Set.of(getRole(request.role())));
        }
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(findUser(id));
    }

    User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getStudentCode(), user.isEnabled(),
                user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()));
    }

    private Role getRole(RoleName name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(Role.builder().name(name).build()));
    }
}

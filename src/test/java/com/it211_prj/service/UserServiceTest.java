package com.it211_prj.service;

import com.it211_prj.dto.user.UserRequest;
import com.it211_prj.dto.user.UserResponse;
import com.it211_prj.entity.Role;
import com.it211_prj.entity.RoleName;
import com.it211_prj.entity.User;
import com.it211_prj.repository.RoleRepository;
import com.it211_prj.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UserService userService;

    @Test
    void createEncodesPasswordAndAssignsRequestedRole() {
        Role role = Role.builder().id(1L).name(RoleName.LECTURER).build();
        when(userRepository.existsByEmail("lecturer@it211.local")).thenReturn(false);
        when(roleRepository.findByName(RoleName.LECTURER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(10L);
            return user;
        });

        UserResponse response = userService.create(new UserRequest("lecturer@it211.local", "secret123", "Lecturer", "GV01", RoleName.LECTURER, true));

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.roles()).contains("LECTURER");
    }
}

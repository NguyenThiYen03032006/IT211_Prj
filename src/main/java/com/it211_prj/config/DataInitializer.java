package com.it211_prj.config;

import com.it211_prj.entity.Role;
import com.it211_prj.entity.RoleName;
import com.it211_prj.entity.User;
import com.it211_prj.repository.RoleRepository;
import com.it211_prj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDefaultData() {
        // Tao role va admin mac dinh de co the dang nhap ngay sau khi chay app.
        return args -> {
            Role adminRole = ensureRole(RoleName.ADMIN);
            ensureRole(RoleName.LECTURER);
            ensureRole(RoleName.STUDENT);
            if (!userRepository.existsByEmail("admin@it211.local")) {
                userRepository.save(User.builder()
                        .email("admin@it211.local")
                        .password(passwordEncoder.encode("admin123"))
                        .fullName("System Admin")
                        .enabled(true)
                        .roles(Set.of(adminRole))
                        .build());
            }
        };
    }

    private Role ensureRole(RoleName name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(Role.builder().name(name).build()));
    }
}

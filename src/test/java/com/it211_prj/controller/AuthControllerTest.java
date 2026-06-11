package com.it211_prj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.it211_prj.dto.auth.RegisterRequest;
import com.it211_prj.dto.user.UserResponse;
import com.it211_prj.service.AuthService;
import com.it211_prj.service.PasswordResetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock AuthService authService;
    @Mock PasswordResetService passwordResetService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void registerReturnsCreatedUser() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService, passwordResetService)).build();
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(new UserResponse(1L, "student@it211.local", "Student", "SV01", true, Set.of("STUDENT")));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequest("student@it211.local", "secret123", "Student", "SV01", null))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("student@it211.local"))
                .andExpect(jsonPath("$.roles[0]").value("STUDENT"));
    }
}

package com.it211_prj.controller;

import com.it211_prj.dto.user.UserResponse;
import com.it211_prj.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock UserService userService;

    @Test
    void searchReturnsPagedUsers() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        when(userService.search(eq("student"), isNull(), any()))
                .thenReturn(new PageImpl<>(List.of(new UserResponse(1L, "student@it211.local", "Student", "SV01", true, Set.of("STUDENT"))), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/v1/users").param("keyword", "student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("student@it211.local"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}

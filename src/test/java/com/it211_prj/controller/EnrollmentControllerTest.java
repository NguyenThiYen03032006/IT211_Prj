package com.it211_prj.controller;

import com.it211_prj.dto.enrollment.EnrollmentResponse;
import com.it211_prj.service.EnrollmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EnrollmentControllerTest {
    @Mock EnrollmentService enrollmentService;

    @Test
    void enrollReturnsCreatedEnrollment() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new EnrollmentController(enrollmentService)).build();
        when(enrollmentService.enrollCurrentStudent(3L))
                .thenReturn(new EnrollmentResponse(5L, 2L, "Student", 3L, "IT211", LocalDateTime.now()));

        mockMvc.perform(post("/api/v1/enrollments/courses/3"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(2))
                .andExpect(jsonPath("$.courseId").value(3));
    }
}

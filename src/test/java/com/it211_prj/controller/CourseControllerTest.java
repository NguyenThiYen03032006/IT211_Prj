package com.it211_prj.controller;

import com.it211_prj.dto.course.CourseResponse;
import com.it211_prj.service.CourseService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {
    @Mock CourseService courseService;

    @Test
    void searchReturnsPagedCourses() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new CourseController(courseService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        CourseResponse course = new CourseResponse(3L, "IT211", "Java", "Spring", 1L, "Lecturer", true);
        when(courseService.search(eq("IT211"), eq(true), any()))
                .thenReturn(new PageImpl<>(List.of(course), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/v1/courses").param("keyword", "IT211").param("active", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code").value("IT211"))
                .andExpect(jsonPath("$.content[0].active").value(true));
    }
}

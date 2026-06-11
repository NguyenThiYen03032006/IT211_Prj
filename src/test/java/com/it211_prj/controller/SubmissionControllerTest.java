package com.it211_prj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.it211_prj.dto.submission.GithubSubmissionRequest;
import com.it211_prj.dto.submission.SubmissionResponse;
import com.it211_prj.service.SubmissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SubmissionControllerTest {
    @Mock SubmissionService submissionService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void submitGithubReturnsCreatedSubmission() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new SubmissionController(submissionService)).build();
        when(submissionService.submitGithub(3L, "Repo", "https://github.com/user/repo"))
                .thenReturn(new SubmissionResponse(8L, 2L, "Student", 3L, "IT211", "Repo", null, "https://github.com/user/repo", LocalDateTime.now()));

        mockMvc.perform(post("/api/v1/submissions/github")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new GithubSubmissionRequest(3L, "Repo", "https://github.com/user/repo"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.githubUrl").value("https://github.com/user/repo"))
                .andExpect(jsonPath("$.fileUrl").doesNotExist());
    }
}

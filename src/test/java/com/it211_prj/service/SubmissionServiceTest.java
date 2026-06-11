package com.it211_prj.service;

import com.it211_prj.dto.submission.SubmissionResponse;
import com.it211_prj.entity.Course;
import com.it211_prj.entity.Submission;
import com.it211_prj.entity.User;
import com.it211_prj.repository.EnrollmentRepository;
import com.it211_prj.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {
    @Mock SubmissionRepository submissionRepository;
    @Mock CourseService courseService;
    @Mock CurrentUserService currentUserService;
    @Mock CloudStorageService cloudStorageService;
    @Mock EnrollmentRepository enrollmentRepository;
    @InjectMocks SubmissionService submissionService;

    @Test
    void submitGithubRequiresEnrollmentAndStoresGithubUrl() {
        User student = User.builder().id(2L).fullName("Student").email("student@it211.local").build();
        Course course = Course.builder().id(3L).title("IT211").lecturer(User.builder().id(1L).fullName("Lecturer").build()).build();
        when(currentUserService.getCurrentUser()).thenReturn(student);
        when(courseService.findCourse(3L)).thenReturn(course);
        when(enrollmentRepository.existsByStudentIdAndCourseId(2L, 3L)).thenReturn(true);
        when(submissionRepository.save(any(Submission.class))).thenAnswer(invocation -> {
            Submission submission = invocation.getArgument(0);
            submission.setId(8L);
            return submission;
        });

        SubmissionResponse response = submissionService.submitGithub(3L, "Repo", "https://github.com/user/repo");

        assertThat(response.id()).isEqualTo(8L);
        assertThat(response.githubUrl()).isEqualTo("https://github.com/user/repo");
        assertThat(response.fileUrl()).isNull();
    }
}

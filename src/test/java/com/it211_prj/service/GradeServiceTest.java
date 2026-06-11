package com.it211_prj.service;

import com.it211_prj.dto.grade.GradeRequest;
import com.it211_prj.dto.grade.GradeResponse;
import com.it211_prj.entity.Course;
import com.it211_prj.entity.Grade;
import com.it211_prj.entity.Submission;
import com.it211_prj.entity.User;
import com.it211_prj.repository.GradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {
    @Mock GradeRepository gradeRepository;
    @Mock SubmissionService submissionService;
    @Mock CurrentUserService currentUserService;
    @InjectMocks GradeService gradeService;

    @Test
    void gradeCreatesGradeForSubmission() {
        User lecturer = User.builder().id(1L).fullName("Lecturer").email("lecturer@it211.local").build();
        User student = User.builder().id(2L).fullName("Student").email("student@it211.local").build();
        Course course = Course.builder().id(3L).title("IT211").lecturer(lecturer).build();
        Submission submission = Submission.builder().id(4L).student(student).course(course).title("HW").fileUrl("file").build();
        when(submissionService.findSubmission(4L)).thenReturn(submission);
        when(currentUserService.getCurrentUser()).thenReturn(lecturer);
        when(gradeRepository.findBySubmissionId(4L)).thenReturn(Optional.empty());
        when(gradeRepository.save(any(Grade.class))).thenAnswer(invocation -> {
            Grade grade = invocation.getArgument(0);
            grade.setId(6L);
            return grade;
        });

        GradeResponse response = gradeService.grade(new GradeRequest(4L, BigDecimal.valueOf(95), "Good"));

        assertThat(response.id()).isEqualTo(6L);
        assertThat(response.submissionId()).isEqualTo(4L);
        assertThat(response.score()).isEqualByComparingTo("95");
    }
}

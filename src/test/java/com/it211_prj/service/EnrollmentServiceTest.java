package com.it211_prj.service;

import com.it211_prj.dto.enrollment.EnrollmentResponse;
import com.it211_prj.entity.Course;
import com.it211_prj.entity.Enrollment;
import com.it211_prj.entity.User;
import com.it211_prj.repository.EnrollmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {
    @Mock EnrollmentRepository enrollmentRepository;
    @Mock CurrentUserService currentUserService;
    @Mock CourseService courseService;
    @InjectMocks EnrollmentService enrollmentService;

    @Test
    void enrollCurrentStudentCreatesEnrollmentWhenNotDuplicate() {
        User student = User.builder().id(2L).fullName("Student").email("student@it211.local").build();
        Course course = Course.builder().id(3L).code("IT211").title("IT211").lecturer(User.builder().id(1L).fullName("Lecturer").build()).build();
        when(currentUserService.getCurrentUser()).thenReturn(student);
        when(courseService.findCourse(3L)).thenReturn(course);
        when(enrollmentRepository.existsByStudentIdAndCourseId(2L, 3L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> {
            Enrollment enrollment = invocation.getArgument(0);
            enrollment.setId(5L);
            return enrollment;
        });

        EnrollmentResponse response = enrollmentService.enrollCurrentStudent(3L);

        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.studentId()).isEqualTo(2L);
        assertThat(response.courseId()).isEqualTo(3L);
    }
}

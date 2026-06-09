package com.it211_prj.service;

import com.it211_prj.dto.enrollment.EnrollmentResponse;
import com.it211_prj.entity.Course;
import com.it211_prj.entity.Enrollment;
import com.it211_prj.entity.User;
import com.it211_prj.exception.BadRequestException;
import com.it211_prj.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CurrentUserService currentUserService;
    private final CourseService courseService;

    @Transactional
    public EnrollmentResponse enrollCurrentStudent(Long courseId) {
        User student = currentUserService.getCurrentUser();
        Course course = courseService.findCourse(courseId);
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new BadRequestException("Student already enrolled in this course");
        }
        Enrollment enrollment = Enrollment.builder().student(student).course(course).build();
        return toResponse(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentResponse> findMine() {
        return enrollmentRepository.findByStudentId(currentUserService.getCurrentUser().getId()).stream().map(this::toResponse).toList();
    }

    public List<EnrollmentResponse> findByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream().map(this::toResponse).toList();
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        return new EnrollmentResponse(enrollment.getId(), enrollment.getStudent().getId(), enrollment.getStudent().getFullName(),
                enrollment.getCourse().getId(), enrollment.getCourse().getTitle(), enrollment.getEnrolledAt());
    }
}

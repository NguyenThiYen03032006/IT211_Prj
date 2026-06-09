package com.it211_prj.controller;

import com.it211_prj.dto.enrollment.EnrollmentResponse;
import com.it211_prj.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    // Student tu ghi danh vao mot khoa hoc.
    @PostMapping("/courses/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponse enroll(@PathVariable Long courseId) {
        return enrollmentService.enrollCurrentStudent(courseId);
    }

    // Student xem cac khoa hoc minh da dang ky.
    @GetMapping("/me")
    public List<EnrollmentResponse> findMine() {
        return enrollmentService.findMine();
    }

    // Admin xem danh sach sinh vien trong mot khoa hoc.
    @GetMapping("/courses/{courseId}")
    public List<EnrollmentResponse> findByCourse(@PathVariable Long courseId) {
        return enrollmentService.findByCourse(courseId);
    }
}

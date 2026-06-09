package com.it211_prj.dto.enrollment;

import java.time.LocalDateTime;

// DTO tra ve thong tin ghi danh.
public record EnrollmentResponse(Long id, Long studentId, String studentName, Long courseId, String courseTitle, LocalDateTime enrolledAt) {
}

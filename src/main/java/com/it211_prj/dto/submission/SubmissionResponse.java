package com.it211_prj.dto.submission;

import java.time.LocalDateTime;

// DTO tra bai nop kem URL file da upload.
public record SubmissionResponse(Long id, Long studentId, String studentName, Long courseId, String courseTitle, String title, String fileUrl, LocalDateTime submittedAt) {
}

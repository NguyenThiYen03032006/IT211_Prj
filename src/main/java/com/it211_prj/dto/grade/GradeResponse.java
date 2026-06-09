package com.it211_prj.dto.grade;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO tra ve diem, feedback va giang vien cham.
public record GradeResponse(Long id, Long submissionId, Long lecturerId, String lecturerName, BigDecimal score, String feedback, LocalDateTime gradedAt) {
}

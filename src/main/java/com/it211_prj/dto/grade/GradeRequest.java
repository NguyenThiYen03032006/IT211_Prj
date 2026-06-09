package com.it211_prj.dto.grade;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// DTO giang vien gui khi cham diem bai nop.
public record GradeRequest(@NotNull Long submissionId, @NotNull @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal score, String feedback) {
}

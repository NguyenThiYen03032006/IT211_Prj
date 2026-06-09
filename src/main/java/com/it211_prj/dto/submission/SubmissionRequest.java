package com.it211_prj.dto.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO metadata cua bai nop; file upload duoc gui bang multipart.
public record SubmissionRequest(@NotNull Long courseId, @NotBlank String title) {
}

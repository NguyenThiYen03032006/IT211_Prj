package com.it211_prj.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO tao/cap nhat khoa hoc.
public record CourseRequest(@NotBlank String code, @NotBlank String title, String description, @NotNull Long lecturerId, Boolean active) {
}

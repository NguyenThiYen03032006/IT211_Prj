package com.it211_prj.dto.material;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO metadata tai lieu; file upload di kem multipart.
public record MaterialRequest(@NotNull Long courseId, @NotBlank String title) {
}

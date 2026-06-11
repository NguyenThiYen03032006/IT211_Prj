package com.it211_prj.dto.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// DTO cho cach nop bai bang GitHub repository thay vi upload file.
public record GithubSubmissionRequest(
        @NotNull Long courseId,
        @NotBlank String title,
        @NotBlank
        @Pattern(regexp = "^https://github\\.com/.+", message = "githubUrl must start with https://github.com/")
        String githubUrl
) {
}

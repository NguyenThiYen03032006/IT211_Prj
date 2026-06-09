package com.it211_prj.dto.material;

import java.time.LocalDateTime;

// DTO tra ve tai lieu hoc tap va URL file.
public record MaterialResponse(Long id, Long courseId, String courseTitle, String title, String fileUrl, Long uploadedBy, LocalDateTime uploadedAt) {
}

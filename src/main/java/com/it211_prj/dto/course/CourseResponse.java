package com.it211_prj.dto.course;

// DTO hien thi thong tin khoa hoc va giang vien phu trach.
public record CourseResponse(Long id, String code, String title, String description, Long lecturerId, String lecturerName, boolean active) {
}

package com.it211_prj.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}),
        indexes = {
                @Index(name = "idx_enrollments_student", columnList = "student_id"),
                @Index(name = "idx_enrollments_course", columnList = "course_id")
        }
)
public class Enrollment {
    // Bang trung gian the hien sinh vien dang ky vao khoa hoc nao.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sinh vien duoc ghi danh.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // Khoa hoc ma sinh vien tham gia.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime enrolledAt = LocalDateTime.now();
}

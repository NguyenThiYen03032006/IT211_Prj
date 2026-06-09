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
@Table(name = "submissions", indexes = @Index(name = "idx_submissions_course_student", columnList = "course_id,student_id"))
public class Submission {
    // Bai nop cua sinh vien trong mot khoa hoc.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sinh vien nop bai.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // Khoa hoc nhan bai nop.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Tieu de bai nop do sinh vien nhap.
    @Column(nullable = false, length = 180)
    private String title;

    // URL file da upload len Cloudinary/S3.
    @Column(nullable = false, length = 600)
    private String fileUrl;

    // Public id de xoa/cap nhat file tren cloud khi can.
    @Column(length = 250)
    private String cloudPublicId;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();
}

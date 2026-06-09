package com.it211_prj.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "grades", uniqueConstraints = @UniqueConstraint(columnNames = "submission_id"))
public class Grade {
    // Diem danh gia cho mot bai nop, moi submission chi co mot grade hien hanh.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan he One-To-One voi bai nop.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false, unique = true)
    private Submission submission;

    // Giang vien cham bai.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = false)
    private User lecturer;

    // Diem so tu 0 den 100, dung BigDecimal de tranh sai so.
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    // Nhan xet cua giang vien cho sinh vien.
    @Column(length = 1000)
    private String feedback;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime gradedAt = LocalDateTime.now();
}

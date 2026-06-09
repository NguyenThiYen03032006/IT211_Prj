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
@Table(name = "materials", indexes = @Index(name = "idx_materials_course", columnList = "course_id"))
public class Material {
    // Tai lieu hoc tap thuoc mot khoa hoc.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(nullable = false, length = 600)
    private String fileUrl;

    @Column(length = 250)
    private String cloudPublicId;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
}

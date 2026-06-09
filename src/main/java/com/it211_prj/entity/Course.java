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
@Table(name = "courses", indexes = @Index(name = "idx_courses_code", columnList = "code"))
public class Course {
    // Khoa chinh cua khoa hoc/lop hoc phan.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ma hoc phan de tim kiem va dang ky.
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    // Ten hoc phan hien thi cho sinh vien.
    @Column(nullable = false, length = 160)
    private String title;

    // Mo ta ngan ve noi dung hoc phan.
    @Column(length = 1000)
    private String description;

    // Giang vien phu trach, quan he Many-To-One tu Course ve User.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = false)
    private User lecturer;

    // Trang thai mo/dong lop hoc phan.
    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

package com.it211_prj.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_users_email", columnList = "email"),
                @Index(name = "idx_users_student_code", columnList = "studentCode")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "studentCode")
        }
)
public class User {
    // Khoa chinh dai dien cho moi tai khoan trong he thong.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Email dung de dang nhap va phai la duy nhat.
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Mat khau da ma hoa bang PasswordEncoder, khong luu plain text.
    @Column(nullable = false)
    private String password;

    // Ho ten hien thi trong cac man hinh quan ly, khoa hoc va cham diem.
    @Column(nullable = false, length = 120)
    private String fullName;

    // Ma sinh vien/giang vien tuy chon, can unique khi co gia tri.
    @Column(unique = true, length = 50)
    private String studentCode;

    // Trang thai khoa/mo tai khoan de admin quan tri truy cap.
    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    // Mot user co the co nhieu role neu can mo rong ve sau.
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Moc thoi gian phuc vu audit/logging.
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

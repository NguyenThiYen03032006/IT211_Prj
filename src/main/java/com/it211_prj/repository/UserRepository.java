package com.it211_prj.repository;

import com.it211_prj.entity.User;
import com.it211_prj.entity.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// Repository thao tac CRUD va truy van dang nhap theo email.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // Tim kiem user theo keyword va role, co Pageable de Admin khong phai tai toan bo bang.
    @Query("""
            select distinct u
            from User u
            join u.roles r
            where (:role is null or r.name = :role)
              and (
                   :keyword is null
                   or lower(u.email) like lower(concat('%', :keyword, '%'))
                   or lower(u.fullName) like lower(concat('%', :keyword, '%'))
                   or lower(coalesce(u.studentCode, '')) like lower(concat('%', :keyword, '%'))
              )
            """)
    Page<User> search(@Param("keyword") String keyword, @Param("role") RoleName role, Pageable pageable);
}

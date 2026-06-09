package com.it211_prj.repository;

import com.it211_prj.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository quan ly sinh vien ghi danh vao khoa hoc.
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long courseId);
}

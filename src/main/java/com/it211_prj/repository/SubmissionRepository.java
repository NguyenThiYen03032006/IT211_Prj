package com.it211_prj.repository;

import com.it211_prj.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository truy van bai nop theo khoa hoc hoac sinh vien.
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudentId(Long studentId);
    List<Submission> findByCourseId(Long courseId);
}

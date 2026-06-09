package com.it211_prj.repository;

import com.it211_prj.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository quan ly diem cho tung bai nop.
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findBySubmissionId(Long submissionId);
}

package com.it211_prj.repository;

import com.it211_prj.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository quan ly khoa hoc va loc theo giang vien.
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCode(String code);
    List<Course> findByLecturerId(Long lecturerId);
}

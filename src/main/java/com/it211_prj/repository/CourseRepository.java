package com.it211_prj.repository;

import com.it211_prj.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Repository quan ly khoa hoc va loc theo giang vien.
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCode(String code);
    List<Course> findByLecturerId(Long lecturerId);

    // Tim kiem khoa hoc theo code/title va loc trang thai active neu client truyen.
    @Query("""
            select c
            from Course c
            where (:active is null or c.active = :active)
              and (
                   :keyword is null
                   or lower(c.code) like lower(concat('%', :keyword, '%'))
                   or lower(c.title) like lower(concat('%', :keyword, '%'))
              )
            """)
    Page<Course> search(@Param("keyword") String keyword, @Param("active") Boolean active, Pageable pageable);
}

package com.it211_prj.repository;

import com.it211_prj.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository quan ly tai lieu hoc tap theo khoa hoc.
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByCourseId(Long courseId);
}

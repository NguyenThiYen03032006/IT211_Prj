package com.it211_prj.controller;

import com.it211_prj.dto.course.CourseRequest;
import com.it211_prj.dto.course.CourseResponse;
import com.it211_prj.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    // Tat ca user da dang nhap co the xem danh sach khoa hoc.
    @GetMapping
    public List<CourseResponse> findAll() {
        return courseService.findAll();
    }

    @GetMapping("/{id}")
    public CourseResponse findById(@PathVariable Long id) {
        return courseService.findById(id);
    }

    // Admin/Giang vien tao khoa hoc.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse create(@Valid @RequestBody CourseRequest request) {
        return courseService.create(request);
    }

    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        return courseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}

package com.it211_prj.controller;

import com.it211_prj.dto.course.CourseRequest;
import com.it211_prj.dto.course.CourseResponse;
import com.it211_prj.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    // Tat ca user da dang nhap co the tim kiem khoa hoc va phan trang.
    @GetMapping
    public Page<CourseResponse> search(@RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) Boolean active,
                                       Pageable pageable) {
        return courseService.search(keyword, active, pageable);
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

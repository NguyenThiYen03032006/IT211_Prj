package com.it211_prj.controller;

import com.it211_prj.dto.grade.GradeRequest;
import com.it211_prj.dto.grade.GradeResponse;
import com.it211_prj.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    // Lecturer/Admin cham diem hoac cap nhat diem cho bai nop.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GradeResponse grade(@Valid @RequestBody GradeRequest request) {
        return gradeService.grade(request);
    }

    // API tong quat xem diem; co the mo rong loc theo student/course.
    @GetMapping
    public List<GradeResponse> findAll() {
        return gradeService.findAll();
    }
}

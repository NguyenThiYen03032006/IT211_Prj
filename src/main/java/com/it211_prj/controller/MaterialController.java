package com.it211_prj.controller;

import com.it211_prj.dto.material.MaterialResponse;
import com.it211_prj.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    // Lecturer/Admin upload tai lieu hoc tap cho khoa hoc.
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public MaterialResponse upload(@RequestParam Long courseId,
                                   @RequestParam String title,
                                   @RequestParam MultipartFile file) {
        return materialService.upload(courseId, title, file);
    }

    // User da dang nhap xem tai lieu cua mot khoa hoc.
    @GetMapping("/courses/{courseId}")
    public List<MaterialResponse> findByCourse(@PathVariable Long courseId) {
        return materialService.findByCourse(courseId);
    }
}

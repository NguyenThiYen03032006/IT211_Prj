package com.it211_prj.controller;

import com.it211_prj.dto.submission.SubmissionResponse;
import com.it211_prj.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    // Student upload bai nop bang multipart/form-data gom courseId, title va file.
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public SubmissionResponse upload(@RequestParam Long courseId,
                                     @RequestParam String title,
                                     @RequestParam MultipartFile file) {
        return submissionService.upload(courseId, title, file);
    }

    // Student xem bai nop cua minh.
    @GetMapping("/me")
    public List<SubmissionResponse> findMine() {
        return submissionService.findMine();
    }

    // Lecturer/Admin xem bai nop theo khoa hoc de cham diem.
    @GetMapping("/courses/{courseId}")
    public List<SubmissionResponse> findByCourse(@PathVariable Long courseId) {
        return submissionService.findByCourse(courseId);
    }
}

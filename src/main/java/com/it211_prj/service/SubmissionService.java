package com.it211_prj.service;

import com.it211_prj.dto.submission.SubmissionResponse;
import com.it211_prj.entity.Course;
import com.it211_prj.entity.Submission;
import com.it211_prj.entity.User;
import com.it211_prj.exception.ResourceNotFoundException;
import com.it211_prj.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final CourseService courseService;
    private final CurrentUserService currentUserService;
    private final CloudStorageService cloudStorageService;

    @Transactional
    public SubmissionResponse upload(Long courseId, String title, MultipartFile file) {
        User student = currentUserService.getCurrentUser();
        Course course = courseService.findCourse(courseId);
        CloudStorageService.UploadResult upload = cloudStorageService.upload(file, "submissions");
        Submission submission = Submission.builder()
                .student(student)
                .course(course)
                .title(title)
                .fileUrl(upload.url())
                .cloudPublicId(upload.publicId())
                .build();
        return toResponse(submissionRepository.save(submission));
    }

    public List<SubmissionResponse> findMine() {
        return submissionRepository.findByStudentId(currentUserService.getCurrentUser().getId()).stream().map(this::toResponse).toList();
    }

    public List<SubmissionResponse> findByCourse(Long courseId) {
        return submissionRepository.findByCourseId(courseId).stream().map(this::toResponse).toList();
    }

    Submission findSubmission(Long id) {
        return submissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Submission not found: " + id));
    }

    SubmissionResponse toResponse(Submission submission) {
        return new SubmissionResponse(submission.getId(), submission.getStudent().getId(), submission.getStudent().getFullName(),
                submission.getCourse().getId(), submission.getCourse().getTitle(), submission.getTitle(), submission.getFileUrl(), submission.getSubmittedAt());
    }
}

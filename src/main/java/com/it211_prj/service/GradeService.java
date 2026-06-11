package com.it211_prj.service;

import com.it211_prj.dto.grade.GradeRequest;
import com.it211_prj.dto.grade.GradeResponse;
import com.it211_prj.entity.Grade;
import com.it211_prj.entity.Submission;
import com.it211_prj.entity.User;
import com.it211_prj.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;
    private final SubmissionService submissionService;
    private final CurrentUserService currentUserService;

    @Transactional
    public GradeResponse grade(GradeRequest request) {
        Submission submission = submissionService.findSubmission(request.submissionId());
        User lecturer = currentUserService.getCurrentUser();
        Grade grade = gradeRepository.findBySubmissionId(request.submissionId()).orElseGet(Grade::new);
        grade.setSubmission(submission);
        grade.setLecturer(lecturer);
        grade.setScore(request.score());
        grade.setFeedback(request.feedback());
        grade.setGradedAt(LocalDateTime.now());
        return toResponse(gradeRepository.save(grade));
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> findAll() {
        return gradeRepository.findAll().stream().map(this::toResponse).toList();
    }

    private GradeResponse toResponse(Grade grade) {
        return new GradeResponse(grade.getId(), grade.getSubmission().getId(), grade.getLecturer().getId(),
                grade.getLecturer().getFullName(), grade.getScore(), grade.getFeedback(), grade.getGradedAt());
    }
}

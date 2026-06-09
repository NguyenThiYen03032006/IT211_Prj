package com.it211_prj.service;

import com.it211_prj.dto.material.MaterialResponse;
import com.it211_prj.entity.Course;
import com.it211_prj.entity.Material;
import com.it211_prj.entity.User;
import com.it211_prj.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final CourseService courseService;
    private final CurrentUserService currentUserService;
    private final CloudStorageService cloudStorageService;

    @Transactional
    public MaterialResponse upload(Long courseId, String title, MultipartFile file) {
        Course course = courseService.findCourse(courseId);
        User uploader = currentUserService.getCurrentUser();
        CloudStorageService.UploadResult upload = cloudStorageService.upload(file, "materials");
        Material material = Material.builder()
                .course(course)
                .uploadedBy(uploader)
                .title(title)
                .fileUrl(upload.url())
                .cloudPublicId(upload.publicId())
                .build();
        return toResponse(materialRepository.save(material));
    }

    public List<MaterialResponse> findByCourse(Long courseId) {
        return materialRepository.findByCourseId(courseId).stream().map(this::toResponse).toList();
    }

    private MaterialResponse toResponse(Material material) {
        return new MaterialResponse(material.getId(), material.getCourse().getId(), material.getCourse().getTitle(),
                material.getTitle(), material.getFileUrl(), material.getUploadedBy().getId(), material.getUploadedAt());
    }
}

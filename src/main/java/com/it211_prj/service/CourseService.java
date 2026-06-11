package com.it211_prj.service;

import com.it211_prj.dto.course.CourseRequest;
import com.it211_prj.dto.course.CourseResponse;
import com.it211_prj.entity.Course;
import com.it211_prj.entity.User;
import com.it211_prj.exception.BadRequestException;
import com.it211_prj.exception.ResourceNotFoundException;
import com.it211_prj.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<CourseResponse> search(String keyword, Boolean active, Pageable pageable) {
        return courseRepository.search(normalize(keyword), active, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public CourseResponse findById(Long id) {
        return toResponse(findCourse(id));
    }

    @Transactional
    public CourseResponse create(CourseRequest request) {
        if (courseRepository.existsByCode(request.code())) {
            throw new BadRequestException("Ma khoa hoc da ton tai");
        }
        User lecturer = userService.findUser(request.lecturerId());
        Course course = Course.builder()
                .code(request.code())
                .title(request.title())
                .description(request.description())
                .lecturer(lecturer)
                .active(request.active() == null || request.active())
                .build();
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = findCourse(id);
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setLecturer(userService.findUser(request.lecturerId()));
        if (request.active() != null) {
            course.setActive(request.active());
        }
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void delete(Long id) {
        courseRepository.delete(findCourse(id));
    }

    Course findCourse(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay khoa hoc: " + id));
    }

    CourseResponse toResponse(Course course) {
        return new CourseResponse(course.getId(), course.getCode(), course.getTitle(), course.getDescription(),
                course.getLecturer().getId(), course.getLecturer().getFullName(), course.isActive());
    }

    private String normalize(String keyword) {
        return keyword == null || keyword.isBlank() ? null : keyword.trim();
    }
}

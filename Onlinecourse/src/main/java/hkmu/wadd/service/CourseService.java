package hkmu.wadd.service;

import hkmu.wadd.model.Course;
import hkmu.wadd.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {
    private static final Logger log = LoggerFactory.getLogger(CourseService.class);
    @Autowired
    private CourseRepository courseRepository;
    private LectureService lectureService;

    public CourseService(@Lazy LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }
    @Transactional
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }
    @Transactional
    public boolean deleteCourse(Long courseId) {
        try {
            courseRepository.deleteById(courseId);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete course: {}", e.getMessage());
            return false;
        }
    }
}
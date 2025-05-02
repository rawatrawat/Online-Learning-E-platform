package hkmu.wadd.service;

import hkmu.wadd.model.Course;
import hkmu.wadd.model.Lecture;
import hkmu.wadd.repository.LectureRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {
    private final LectureRepository lectureRepository;
    private final CourseService courseService;


    public LectureService(LectureRepository lectureRepository, CourseService courseService) {
        this.lectureRepository = lectureRepository;
        this.courseService = courseService;
    }

    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    public Lecture getLectureById(Integer id) {
        return lectureRepository.findById(id).orElse(null);
    }

    @Transactional
    public Lecture addLecture(Lecture lecture, Long courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }
        lecture.setCourse(course);
        return lectureRepository.save(lecture);
    }


    @Transactional
    public Lecture addLecture(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    public void deleteLecture(Integer id) {
        lectureRepository.deleteById(id);
    }

    @Transactional
    public Lecture updateLectureFiles(Integer id, String lectureFileName, String exerciseFileName) {
        Lecture lecture = lectureRepository.findById(id).orElse(null);
        if (lecture != null) {
            if (lectureFileName != null) {
                lecture.setLectureFileName(lectureFileName);
            }
            if (exerciseFileName != null) {
                lecture.setExerciseFileName(exerciseFileName);
            }
            return lectureRepository.save(lecture);
        }
        return null;
    }

    public List<Lecture> getLecturesByCourseId(Long courseId) {
        return lectureRepository.findByCourseId(courseId);
    }
}
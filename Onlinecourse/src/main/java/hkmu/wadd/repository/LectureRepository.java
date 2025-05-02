package hkmu.wadd.repository;
import hkmu.wadd.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    List<Lecture> findByCourseId(Long courseId);
}

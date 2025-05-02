package hkmu.wadd.repository;
import hkmu.wadd.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByLectureId(int lectureId);
}

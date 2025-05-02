package hkmu.wadd.repository;

import hkmu.wadd.model.PollComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollCommentRepository extends JpaRepository<PollComment, Long> {
}

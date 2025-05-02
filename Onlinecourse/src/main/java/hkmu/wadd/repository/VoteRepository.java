package hkmu.wadd.repository;

import hkmu.wadd.model.Poll;
import hkmu.wadd.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByUser(hkmu.wadd.model.User user);
    void deleteByPollAndUser(Poll poll, hkmu.wadd.model.User user);
    boolean existsByPollAndUser(Poll poll, hkmu.wadd.model.User user);
}
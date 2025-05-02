package hkmu.wadd.service;

import hkmu.wadd.model.Poll;
import hkmu.wadd.model.PollComment;
import hkmu.wadd.model.User;
import hkmu.wadd.repository.PollCommentRepository;
import hkmu.wadd.repository.PollRepository;
import hkmu.wadd.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollCommentRepository pollcommentRepository;

    @Transactional
    public void createPoll(Poll poll) {
        pollRepository.save(poll);
    }

    public Poll getPollById(Long id) {
        return pollRepository.findById(id).orElse(null);
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    @Transactional
    public void updatePoll(Poll poll) {
        pollRepository.save(poll);
    }

    @Transactional
    public void deletePoll(Long id) {
        pollRepository.deleteById(id);
    }

    @Transactional
    public void addVote(Long pollId, int optionIndex, User user) {
        Poll poll = getPollById(pollId);
        if (poll != null) {
            if (voteRepository.existsByPollAndUser(poll, user)) {
                throw new IllegalStateException("You have already voted in this poll");
            }
            poll.addVote(optionIndex, user);
            updatePoll(poll);
        }
    }


    @Transactional
    public void removeVote(Long pollId, User user) {
        Poll poll = getPollById(pollId);
        if (poll != null) {
            voteRepository.deleteByPollAndUser(poll, user);
            poll.removeVote(user);
            updatePoll(poll);
        }
    }

    @Transactional
    public void addComment(Long pollId, String text, User user) {
        Poll poll = pollRepository.findById(pollId).orElse(null);
        if (poll != null) {
            PollComment comment = new PollComment(text, poll, user);
            pollcommentRepository.save(comment);
        }
    }

    @Transactional
    public void removeComment(Long commentId, User user) {
        Optional<PollComment> commentOptional = pollcommentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            PollComment comment = commentOptional.get();

            if (comment.getUser().equals(user) || user.getRole().equals("TEACHER")) {
                pollcommentRepository.deleteById(commentId);
            } else {
                throw new IllegalArgumentException("You do not have permission to delete this comment.");
            }
        }
    }
}



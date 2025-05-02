package hkmu.wadd.service;
import hkmu.wadd.model.Comment;
import hkmu.wadd.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByLectureId(int lectureId) {
        return commentRepository.findByLectureId(lectureId);
    }

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

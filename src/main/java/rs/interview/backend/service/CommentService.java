package rs.interview.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.interview.backend.domain.City;
import rs.interview.backend.domain.Comment;

import java.util.List;


public interface CommentService {
    Page<Comment> findAll(Pageable pageable);

    Comment findById(Long id);

    Comment findCommentByIdAndCity(Long id, City city);

    Page<Comment> findCommentsByCity(City city, Pageable pageable);

    Comment save(Comment comment);

    void deleteComment(Long commentId);
}

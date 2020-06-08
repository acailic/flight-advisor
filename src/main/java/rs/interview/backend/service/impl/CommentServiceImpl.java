package rs.interview.backend.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.interview.backend.domain.City;
import rs.interview.backend.domain.Comment;
import rs.interview.backend.repository.CommentRepository;
import rs.interview.backend.service.CommentService;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;


    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Page<Comment> findCommentsByCity(City city, Pageable pageable) {
        return commentRepository.findCommentsByCity(city, pageable);
    }

    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Comment findCommentByIdAndCity(Long commentId, City city) {
        return commentRepository.findCommentByIdAndCity(commentId, city);
    }
}

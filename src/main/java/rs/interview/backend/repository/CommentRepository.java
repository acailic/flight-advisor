package rs.interview.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.interview.backend.domain.City;
import rs.interview.backend.domain.Comment;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAll(Pageable pageable);

    List<Comment> findCommentsByCityOrderByModifiedAtDesc(City city, Pageable pageable);

    Comment findCommentByIdAndCity(Long id, City city);

    Page<Comment> findCommentsByCity(City city,Pageable pageable);
}

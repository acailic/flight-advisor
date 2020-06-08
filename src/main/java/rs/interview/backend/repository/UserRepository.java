package rs.interview.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.interview.backend.domain.User;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 * @author a.ilic
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByUsername(String username);
}

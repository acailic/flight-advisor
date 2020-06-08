package rs.interview.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.interview.backend.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 * @author a.ilic
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}

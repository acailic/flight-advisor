package rs.interview.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.interview.backend.domain.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}

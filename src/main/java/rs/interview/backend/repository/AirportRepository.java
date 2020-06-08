package rs.interview.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.interview.backend.domain.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    Airport findAirportByExternalId(Long externalId);
}

package rs.interview.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.interview.backend.domain.City;
import rs.interview.backend.repository.projections.CityView;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Page<City> findCityByNameContains(String name, Pageable pageable);

    Page<City> findByNameContainingIgnoreCase(String name, Pageable pageable);


    Page<CityView> getCitiesByNameContainingIgnoreCase(String name, Pageable pageable);
}

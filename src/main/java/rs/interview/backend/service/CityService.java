package rs.interview.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import rs.interview.backend.domain.City;

import java.util.stream.DoubleStream;


public interface CityService {
    Page<City> findAll(Pageable pageable);

    Page<City> findByNameContainingIgnoreCase(String name, Pageable pageable);


    Page<City> findByNameContainingIgnoreCaseLimitComments(String name, Integer commentLimit, Pageable pageable);

    City findById(Long id);

    void save(City city);
}

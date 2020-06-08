package rs.interview.backend.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.interview.backend.domain.City;
import rs.interview.backend.domain.Comment;
import rs.interview.backend.repository.CityRepository;
import rs.interview.backend.repository.CommentRepository;
import rs.interview.backend.repository.projections.CityView;
import rs.interview.backend.service.CityService;
import rs.interview.backend.service.mapper.CityMapper;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CityServiceImpl implements CityService {


    private final CityRepository cityRepository;
    private final CommentRepository commentRepository;
    private final CityMapper cityMapper;

    public CityServiceImpl(CityRepository cityRepository, CommentRepository commentRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.commentRepository = commentRepository;
        this.cityMapper = cityMapper;
    }

    @Override
    public Page<City> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Override
    public Page<City> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return cityRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<City> findByNameContainingIgnoreCaseLimitComments(String name, Integer commentLimit,
                                                                  Pageable pageable) {
        List<City> cityList = new ArrayList<>();
        Page<CityView> cities = cityRepository.getCitiesByNameContainingIgnoreCase(name, pageable);
        cities.getContent().forEach(cityView -> {
            City city = cityMapper.cityViewToCity(cityView);
            List<Comment> comments =
                    commentRepository.findCommentsByCityOrderByModifiedAtDesc(city, PageRequest.of(0, commentLimit));
            city.setComments(comments);
            cityList.add(city);

        });
        return new PageImpl<>(cityList, pageable, cityList.size());
    }

    @Override
    public City findById(Long id) {
        return cityRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void save(City city) {
        cityRepository.save(city);
    }


}

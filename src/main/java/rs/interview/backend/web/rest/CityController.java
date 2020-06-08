package rs.interview.backend.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.interview.backend.domain.City;
import rs.interview.backend.security.AuthoritiesConstants;
import rs.interview.backend.service.CityService;
import rs.interview.backend.service.dto.CityDTO;
import rs.interview.backend.service.mapper.CityMapper;

@RestController
@RequestMapping("/api")
public class CityController {


    private final CityService cityService;
    private final CityMapper cityMapper;

    public CityController(CityService cityService, CityMapper cityMapper) {
        this.cityService = cityService;
        this.cityMapper = cityMapper;
    }

    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    @GetMapping("/cities")
    public ResponseEntity<Page<CityDTO>> getCities(@RequestParam(required = false) String searchByName,
                                                   @RequestParam(required = false) Integer limitComments,
                                                   Pageable pageable) {
        Page<CityDTO> cityDTOPage;
        searchByName = StringUtils.isBlank(searchByName) ? "" : searchByName;
        if (limitComments == null) {
            cityDTOPage = cityService.findByNameContainingIgnoreCase(searchByName, pageable).map(cityMapper::toDto);

        } else {
            cityDTOPage = cityService.findByNameContainingIgnoreCaseLimitComments(searchByName, limitComments,
                    pageable).map(cityMapper::toDto);
        }
        return ResponseEntity.ok().body(cityDTOPage);
    }

    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    @GetMapping("/cities/{id}")
    public ResponseEntity<CityDTO> getCity(@PathVariable Long id) {
        City city = cityService.findById(id);
        CityDTO cityDTO = cityMapper.toDto(city);
        return ResponseEntity.ok().body(cityDTO);
    }

    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PostMapping("/cities")
    public ResponseEntity<CityDTO> addCity(@RequestBody CityDTO cityDTO) {
        cityDTO.setId(null);
        City city = cityMapper.toEntity(cityDTO);
        cityService.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(cityMapper.toDto(city));
    }
}

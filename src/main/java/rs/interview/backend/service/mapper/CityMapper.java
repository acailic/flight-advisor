package rs.interview.backend.service.mapper;

import org.apache.commons.csv.CSVRecord;
import org.mapstruct.Mapper;
import rs.interview.backend.domain.City;
import rs.interview.backend.repository.projections.CityView;
import rs.interview.backend.service.dto.CityDTO;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface CityMapper extends EntityMapper<CityDTO, City>{

    CityDTO toDto(City city);


    City toEntity(CityDTO cityDTO);


    default City fromId(Long id) {
        if (id == null) {
            return null;
        }
        City city = new City();
        city.setId(id);
        return city;
    }

    default City csvRecordToCity(CSVRecord csvRecord) {
        City city = new City();
        city.setName(csvRecord.get(2));
        city.setCountry(csvRecord.get(3));
        city.setDescription("");
        return city;
    }

    default City cityViewToCity(CityView cityView) {
        City city = new City();
        city.setId(cityView.getId());
        city.setName(cityView.getName());
        city.setCountry(cityView.getCountry());
        city.setDescription(cityView.getDescription());
        return city;
    }
}
